package com.nagaja.the330.view.recruitmentdetail

import android.os.Bundle
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage

class RecruitJobsDetailFragment : BaseFragment() {
    private lateinit var viewModel: RecruitJobsDetailVM

    companion object {
        fun newInstance(id: Int) = RecruitJobsDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[RecruitJobsDetailVM::class.java]
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
                            viewModel.getRecruitJobsDetail(
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
            Header(stringResource(R.string.recruitment_jobsearch)) {
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
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            viewModel.recruitJobsModel.value.title ?: "",
                            color = ColorUtils.gray_222222,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            "${stringResource(R.string.views)} ${viewModel.recruitJobsModel.value.viewCount ?: 0}",
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                    }
                    Divider(
                        color = ColorUtils.black_000000_opacity_5,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp
                    GlideImage(
                        imageModel = "${BuildConfig.BASE_S3}${
                            viewModel.recruitJobsModel.value.images?.getOrNull(
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

                    Text(
                        "${viewModel.recruitJobsModel.value.city?.name?.getOrNull(0)?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        "${viewModel.recruitJobsModel.value.district?.name?.getOrNull(0)?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "아이디: ${viewModel.recruitJobsModel.value.writer?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "연락처: ${viewModel.recruitJobsModel.value.writer?.phone}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "SNS: ${viewModel.recruitJobsModel.value.snsInfo}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    //TODO: Body
                    Text(
                        HtmlCompat.fromHtml(
                            viewModel.recruitJobsModel.value.body ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                            .toString(),
                        style = text14_222,
                        modifier = Modifier.padding(top = 11.dp, bottom = 20.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(ColorUtils.gray_F5F5F5)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                ButtonFunction(
                    Modifier.weight(1f),
                    "수정",
                    ColorUtils.blue_2177E4,
                    ColorUtils.white_FFFFFF,
                    ColorUtils.blue_2177E4
                )
                ButtonFunction(
                    Modifier.weight(1f),
                    "취소",
                    ColorUtils.gray_222222,
                    ColorUtils.gray_222222,
                    ColorUtils.white_FFFFFF
                )
                ButtonFunction(
                    Modifier.weight(1f),
                    "수정",
                    ColorUtils.blue_2177E4,
                    ColorUtils.blue_2177E4,
                    ColorUtils.white_FFFFFF
                )
            }
        }
    }

    @Composable
    private fun ButtonFunction(
        modifier: Modifier = Modifier,
        text: String,
        borderColor: Color,
        backgroundColor: Color,
        textColor: Color
    ) {
        Box(
            modifier
                .fillMaxHeight()
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = borderColor
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = textColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}