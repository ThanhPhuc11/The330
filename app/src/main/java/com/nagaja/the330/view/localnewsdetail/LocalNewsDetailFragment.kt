package com.nagaja.the330.view.localnewsdetail

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.view.*
import com.nagaja.the330.view.comment.CommentInput
import com.nagaja.the330.view.comment.CommentList
import com.nagaja.the330.view.comment.CommentVM
import com.skydoves.landscapist.glide.GlideImage

class LocalNewsDetailFragment : BaseFragment() {
    private lateinit var viewModel: LocalNewsDetailVM
    private lateinit var commentViewModel: CommentVM

    companion object {
        fun newInstance(id: Int) = LocalNewsDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[LocalNewsDetailVM::class.java]
        commentViewModel = getViewModelProvider(this)[CommentVM::class.java]
        viewController = (activity as MainActivity).viewController
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
                            commentViewModel.getCommentsById(
                                it,
                                0,
                                "ACTIVATED",
                                null,
                                requireArguments().getInt(AppConstants.EXTRA_KEY1),
                                null
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
            Header(stringResource(R.string.local_news)) {
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
                        viewModel.localNewsModel.value.title ?: "",
                        color = ColorUtils.gray_222222,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            AppDateUtils.changeDateFormat(
                                AppDateUtils.FORMAT_16,
                                AppDateUtils.FORMAT_15,
                                viewModel.localNewsModel.value.createdOn ?: ""
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
                            stringResource(R.string.views).plus(" ${viewModel.localNewsModel.value.viewCount ?: 0}"),
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
                        imageModel = "${BuildConfig.BASE_S3}${""}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((200 * screenWidth / 343).dp)
                            .clip(RoundedCornerShape(4.dp)),
                        placeHolder = painterResource(R.drawable.ic_default_nagaja),
                        error = painterResource(R.drawable.ic_default_nagaja)
                    )
                    //TODO: Body
                    Text(
                        HtmlCompat.fromHtml(
                            viewModel.localNewsModel.value.body ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                            .toString(),
                        style = text14_62,
                        modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
                    )
                }
                val stateShowDialog = remember { mutableStateOf(false) }
                val stateSelectedId = remember { mutableStateOf(0) }
                val lazyListState = rememberLazyListState()
                CommentList(
                    commentViewModel.stateCommentCount.value,
                    commentViewModel.stateListComment,
                    lazyListState,
                    userDetailBase?.id ?: 0
                ) {
                    stateShowDialog.value = true
                    stateSelectedId.value = it
                }

                LoadmoreHandler(lazyListState) { page->
                    commentViewModel.getCommentsById(
                        accessToken!!,
                        page,
                        "ACTIVATED",
                        null,
                        requireArguments().getInt(AppConstants.EXTRA_KEY1),
                        null
                    )
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

    @Preview
    @Composable
    private fun ItemComment() {
        Column(
            Modifier
                .fillMaxWidth()
                .background(ColorUtils.white_FFFFFF)
                .padding(16.dp)
        ) {
            Text("mintkim", style = text14_222)
            Text("2021. 10. 18", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp)
            Text("우와 넘 좋은 소식이네요.", style = text14_62, modifier = Modifier.padding(top = 5.dp))
        }
    }

    @Preview
    @Composable
    private fun ItemCommentOwner() {
        Column(
            Modifier
                .fillMaxWidth()
                .background(ColorUtils.blue_2177E4_opacity_5)
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("mintkim", style = text14_222)
                Spacer(Modifier.weight(1f))
                Image(painter = painterResource(R.drawable.ic_more), contentDescription = null,
                    modifier = Modifier.noRippleClickable {

                    })
            }
            Text("2021. 10. 18", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp)
            Text("우와 넘 좋은 소식이네요.", style = text14_62, modifier = Modifier.padding(top = 5.dp))
        }
    }
}