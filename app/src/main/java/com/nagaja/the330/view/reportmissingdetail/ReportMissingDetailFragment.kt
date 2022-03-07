package com.nagaja.the330.view.reportmissingdetail

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage

class ReportMissingDetailFragment : BaseFragment() {
    private lateinit var viewModel: ReportMissingDetailVM

    companion object {
        fun newInstance(id: Int) = ReportMissingDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ReportMissingDetailVM::class.java]
        viewController = (activity as MainActivity).viewController

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
                            viewModel.getLocalNewsDetail(
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
            Header(stringResource(R.string.report_missing)) {
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
                    Row(
                        Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .padding(end = 8.dp)
                                .size(55.dp, 28.dp)
                                .border(
                                    width = 1.dp,
                                    color = ColorUtils.blue_2177E4,
                                    shape = RoundedCornerShape(99.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (viewModel.reportMissingModel.value.type == "REPORT")
                                    stringResource(R.string.report)
                                else stringResource(R.string.missing),
                                color = ColorUtils.blue_2177E4,
                                fontSize = 14.sp
                            )
                        }

                        Text(
                            viewModel.reportMissingModel.value.title ?: "",
                            color = ColorUtils.gray_222222,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${viewModel.reportMissingModel.value.writer?.name}",
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
                                viewModel.reportMissingModel.value.createdOn ?: ""
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
                                .plus(" ${viewModel.reportMissingModel.value.viewCount ?: 0}"),
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
                            viewModel.reportMissingModel.value.images?.getOrNull(
                                0
                            )?.url
                        }",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((200 * screenWidth / 343).dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeHolder = painterResource(R.drawable.ic_default_nagaja),
                        error = painterResource(R.drawable.ic_default_nagaja)
                    )
                    //TODO: Register (company or user)
                    val name = if (viewModel.reportMissingModel.value.writer?.type == "COMPANY") {
                        viewModel.reportMissingModel.value.writer?.companyRequest?.name?.getOrNull(0)?.name
                            ?: ""
                    } else {
                        viewModel.reportMissingModel.value.writer?.name ?: ""
                    }
                    Text(
                        "등록기관: $name",
                        style = text14_62,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    //TODO: Body
                    Text(
                        HtmlCompat.fromHtml(
                            viewModel.reportMissingModel.value.body ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                            .toString(),
                        style = text14_222,
                        modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                    )
                }

                CommentList(viewModel.reportMissingModel.value.commentCount)
            }
            CommentInput()
        }
    }
}