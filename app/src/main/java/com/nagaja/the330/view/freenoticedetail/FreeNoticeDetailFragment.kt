package com.nagaja.the330.view.freenoticedetail

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.CommentModel
import com.nagaja.the330.model.IdentityModel
import com.nagaja.the330.model.ReportModel
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.nagaja.the330.view.comment.CommentInput
import com.nagaja.the330.view.comment.CommentList
import com.nagaja.the330.view.comment.CommentVM
import com.skydoves.landscapist.glide.GlideImage

class FreeNoticeDetailFragment : BaseFragment() {
    private lateinit var viewModel: FreeNoticeDetailVM
    private lateinit var commentViewModel: CommentVM

    companion object {
        fun newInstance(id: Int) = FreeNoticeDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FreeNoticeDetailVM::class.java]
        commentViewModel = getViewModelProvider(this)[CommentVM::class.java]
        viewController = (activity as MainActivity).viewController


        viewModel.callbackReportSuccess.observe(viewLifecycleOwner) {
            showMess("Reported!")
        }
        viewModel.callbackStart.observe(viewLifecycleOwner) {
            showLoading()
        }
        viewModel.callbackSuccess.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.callbackFail.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.showMessCallback.observe(viewLifecycleOwner) {
            showMess(it)
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            viewModel.getFreeNotiDetail(
                                it,
                                requireArguments().getInt(AppConstants.EXTRA_KEY1)
                            )
                        }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        LayoutTheme330 {
            Header(stringResource(R.string.free_board)) {
                viewController?.popFragment()
            }
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        viewModel.freeNoticeDetailModel.value.title ?: "",
                        color = ColorUtils.gray_222222,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${viewModel.freeNoticeDetailModel.value.user?.name}",
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_dot),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(2.dp),
                            colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                        )
                        Text(
                            AppDateUtils.changeDateFormat(
                                AppDateUtils.FORMAT_16,
                                AppDateUtils.FORMAT_15,
                                viewModel.freeNoticeDetailModel.value.createdOn ?: ""
                            ),
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_dot),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(2.dp),
                            colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                        )
                        Text(
                            stringResource(R.string.views)
                                .plus(" ${viewModel.freeNoticeDetailModel.value.viewCount ?: 0}"),
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                    }
                    Divider(
                        color = ColorUtils.black_000000_opacity_5,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp
                    GlideImage(
                        imageModel = "${BuildConfig.BASE_S3}${
                            viewModel.freeNoticeDetailModel.value.images?.getOrNull(
                                0
                            )?.url ?: ""
                        }",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((200 * screenWidth / 343).dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeHolder = painterResource(R.drawable.ic_default_nagaja),
                        error = painterResource(R.drawable.ic_default_nagaja)
                    )

                    Row(Modifier.padding(top = 25.dp)) {
                        Column(Modifier.padding(end = 30.dp)) {
                            Text(stringResource(R.string.company_name), style = text14_222)
                            Text(stringResource(R.string.address), style = text14_222, modifier = Modifier.padding(top = 10.dp))
                            Text(
                                stringResource(R.string.phone_number),
                                style = text14_222,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                        Column {
                            Text(
                                "${viewModel.freeNoticeDetailModel.value.companyName} (세부시의)",
                                style = text14_62
                            )
                            Text(
                                "${viewModel.freeNoticeDetailModel.value.address}",
                                style = text14_62,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                "${viewModel.freeNoticeDetailModel.value.contact}",
                                style = text14_62,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }
                    }
                    //TODO: Body
                    Text(
                        HtmlCompat.fromHtml(
                            viewModel.freeNoticeDetailModel.value.body ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                            .toString(),
                        style = text14_62,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }

                Divider(color = ColorUtils.gray_E1E1E1)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        stringResource(R.string.chat_inquiry),
                        style = text14_62
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_chat_tab),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(17.dp),
                        colorFilter = ColorFilter.tint(ColorUtils.black_000000)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_dot),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(2.dp),
                        colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                    )
                    val stateDialogReport = remember { mutableStateOf(false) }
                    if (stateDialogReport.value) {
                        DialogReport(
                            state = stateDialogReport,
                            onClick = { isConfirmed, content ->
                                if (isConfirmed) {
                                    viewModel.reportFreeNotice(
                                        accessToken!!,
                                        ReportModel().apply {
                                            post = IdentityModel().apply {
                                                id = viewModel.freeNoticeDetailModel.value.id
                                            }
                                            reason = content
                                        }
                                    )
                                }
                            }
                        )
                    }
                    Row(
                        Modifier.noRippleClickable {
                            stateDialogReport.value = true
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.report),
                            style = text14_62
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp),
                            colorFilter = ColorFilter.tint(ColorUtils.black_000000)
                        )
                    }
                }

                val stateShowDialog = remember { mutableStateOf(false) }
                val stateSelectedId = remember { mutableStateOf(0) }
                CommentList(
                    commentViewModel.stateCommentCount.value,
                    commentViewModel.stateListComment,
                    userDetailBase?.id ?: 0
                ) {
                    stateShowDialog.value = true
                    stateSelectedId.value = it
                }

                if (stateShowDialog.value) {
                    DialogCancelComment(state = stateShowDialog, onClick = {
                        if (it) {
                            commentViewModel.editComments(accessToken!!, stateSelectedId.value)
                        }
                    })
                }
            }
            CommentInput {
                commentViewModel.postComments(
                    accessToken!!,
                    CommentModel().apply {
                        body = it
                        localNews = IdentityModel().apply {
                            id = requireArguments().getInt(AppConstants.EXTRA_KEY1)
                        }
                    }
                )
            }
        }
    }
}