package com.nagaja.the330.view.secondhanddetail

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage

class SecondHandDetailFragment : BaseFragment() {
    private lateinit var viewModel: SecondHandDetailVM
    private var id: Int? = null

    companion object {
        fun newInstance(id: Int) = SecondHandDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SecondHandDetailVM::class.java]
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
                        id = requireArguments().getInt(AppConstants.EXTRA_KEY1)
                        viewModel.getDetail(accessToken!!, id!!)
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
            Header("") {
                viewController?.popFragment()
            }
            Column(
                Modifier
                    .verticalScroll(state = rememberScrollState())
                    .padding(bottom = 50.dp)
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp
                    GlideImage(
                        imageModel = "${BuildConfig.BASE_S3}${viewModel.secondhandDetail.value.images?.getOrNull(0)?.url}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenWidth),
                        placeHolder = painterResource(R.drawable.ic_default_nagaja),
                        error = painterResource(R.drawable.ic_default_nagaja)
                    )

                    Divider(
                        color = ColorUtils.gray_E1E1E1,
                        modifier = Modifier.padding(top = 40.dp)
                    )

                    //TODO: Title
                    Text(
                        viewModel.secondhandDetail.value.title ?: "",
                        color = ColorUtils.gray_222222,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Row(
                        Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //TODO: User
                        val textUserName = viewModel.secondhandDetail.value.seller?.name ?: ""
                        val textCity =
                            viewModel.secondhandDetail.value.city?.name?.get(0)?.name ?: ""
                        val textDistrict =
                            viewModel.secondhandDetail.value.district?.name?.get(0)?.name ?: ""
                        Text(
                            "$textUserName ($textCity, $textDistrict)",
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            AppDateUtils.changeDateFormat(
                                AppDateUtils.FORMAT_16,
                                AppDateUtils.FORMAT_15,
                                viewModel.secondhandDetail.value.createdOn ?: ""
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
                            "조회수 ${viewModel.secondhandDetail.value.viewCount ?: 0}",
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                    }

                    Divider(
                        color = ColorUtils.gray_E1E1E1,
                        modifier = Modifier.padding(top = 14.dp)
                    )

                    //TODO: Body post
                    Text(
                        viewModel.secondhandDetail.value.body ?: "",
                        style = text14_222,
                        modifier = Modifier.padding(vertical = 15.dp),
                        textAlign = TextAlign.Start
                    )
                }
                Divider(color = ColorUtils.gray_E1E1E1)
                Row(
                    Modifier.padding(vertical = 15.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        price(),
                        color = ColorUtils.black_000000,
                        fontSize = 22.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Box(
                        Modifier
                            .size(120.dp, 40.dp)
                            .background(
                                color = ColorUtils.blue_2177E4,
                                shape = RoundedCornerShape(2.dp)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "상담하기",
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500)
                        )
                    }
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }

    private fun price(): String {
        val secondhand = viewModel.secondhandDetail.value
        return if ((secondhand.dollar ?: 0.0) > 0) {
            GetDummyData.getMoneyType()[1].name!!.plus(" ").plus(secondhand.dollar)
        } else {
            GetDummyData.getMoneyType()[0].name!!.plus(" ").plus(secondhand.peso)
        }
    }
}