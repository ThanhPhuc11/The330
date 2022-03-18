package com.nagaja.the330.view.companydetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.ProductModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.CommonUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.chatdetail.ChatDetailFragment
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CompanyDetailFragment : BaseFragment() {
    private lateinit var viewModel: CompanyDetailVM
    private var userDetail1 = mutableStateOf(UserDetail())

    companion object {
        fun newInstance(id: Int) = CompanyDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, id)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[CompanyDetailVM::class.java]
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

    @OptIn(ExperimentalPagerApi::class)
    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        getUserDetailFromDataStore(requireContext())
                        accessToken?.let {
                            viewModel.getCompanyDetail(
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
        val pagerState = rememberPagerState(pageCount = 2)
        LayoutTheme330 {
            HeaderSearch(
                clickBack = {
                    viewController?.popFragment()
                }
            )
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                GlideImage(
                    imageModel = "",
                    contentDescription = "",
                    placeHolder = painterResource(R.drawable.ic_default_nagaja),
                    error = painterResource(R.drawable.ic_default_nagaja),
                    requestOptions = {
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerInside()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenWidth * 282 / 375).dp)
                )

                Text(
                    viewModel.companyDetail.value.name?.getOrNull(0)?.name ?: "",
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 13.dp)
                        .padding(horizontal = 16.dp)
                        .noRippleClickable {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_CHAT_DETAIL,
                                ChatDetailFragment.newInstance()
                            )
                        }
                )
                Row(Modifier.padding(horizontal = 16.dp, vertical = 25.dp)) {
                    ButtonLike(
                        "추천",
                        R.drawable.ic_like,
                        10
                    ) {}
                    Spacer(modifier = Modifier.width(8.dp))
                    ButtonLike(
                        "단골",
                        if (viewModel.isFollowing.value) R.drawable.ic_tim else R.drawable.ic_heart_empty,
                        viewModel.companyDetail.value.likedCount ?: 0
                    ) {
                        accessToken?.let { viewModel.followOrNot(it) }
                    }
                }

                Row(
                    Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(49.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    TabSelected(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.basic_information),
                        isSelected = pagerState.currentPage == 0
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.scrollToPage(0)
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(ColorUtils.gray_E1E1E1)
                    )
                    TabSelected(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.jobsearch),
                        isSelected = pagerState.currentPage == 1
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.scrollToPage(1)
                        }
                    }
                }
                SetupPager(pagerState)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(ColorUtils.blue_2177E4),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("오늘마감", color = ColorUtils.blue_D3E4FA, fontSize = 10.sp)
                    Text(
                        "예약하기",
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(ColorUtils.gray_222222)
                        .noRippleClickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!CommonUtils.hasPermissions(
                                        context,
                                        CommonUtils.callphonePermission
                                    )
                                ) {
                                    callbackPermission.launch(CommonUtils.callphonePermission)
                                    return@noRippleClickable
                                }
                                val phoneNum = viewModel.companyDetail.value.chargePhone
                                startActivity(Intent(Intent.ACTION_CALL).apply {
                                    data = Uri.parse("tel:${phoneNum}")
                                })
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.make_a_phone_call),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    @Composable
    private fun TabSelected(
        modifier: Modifier = Modifier,
        text: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        if (isSelected)
            Column(
                modifier
                    .background(ColorUtils.white_FFFFFF)
                    .fillMaxHeight()
                    .noRippleClickable {
                        onClick.invoke()
                    },
            ) {
                Divider(color = ColorUtils.gray_222222, thickness = 2.dp)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text,
                        color = ColorUtils.gray_222222,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(color = ColorUtils.white_FFFFFF, thickness = 1.dp)
            }
        else
            Column(
                modifier
                    .background(ColorUtils.white_FFFFFF)
                    .fillMaxHeight()
                    .noRippleClickable {
                        onClick.invoke()
                    },
            ) {
                Divider(color = ColorUtils.white_FFFFFF, thickness = 2.dp)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text,
                        color = ColorUtils.gray_222222,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider(color = ColorUtils.gray_E1E1E1, thickness = 1.dp)
            }

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .heightIn(
                    0.dp, 600.dp
                )
                .wrapContentHeight()
        ) { page ->
            if (page == 0) {
                CompanyInfo(viewModel.companyDetail.value)
            } else {
                ProductList()
            }
        }
    }

    @Composable
    private fun CompanyInfo(obj: CompanyModel) {
        Column(
            Modifier
                .padding(horizontal = 19.dp)
                .padding(top = 37.dp)
                .verticalScroll(rememberScrollState())
        ) {
            RowDataInfo("업체명", obj.name?.getOrNull(0)?.name ?: "")
            RowDataInfo(
                "주소",
                "${obj.city?.name?.getOrNull(0)?.name} ${obj.district?.name?.getOrNull(0)?.name}"
            )
            RowDataInfo("전화번호", "${obj.chargePhone}")
            RowDataInfo("배달 여부", "delivery")
            RowDataInfo("예약가능 여부", "${obj.reservationTime?.getOrNull(0)}")
            RowDataInfo("픽업가능 여부", "pickup")
            RowDataInfo("드랍가능 여부", "drop available")
            RowDataInfo("예약가능 인원", "${obj.reservationNumber}")
            RowDataInfo("결제 수단", "${obj.paymentMethod}")
        }
    }

    @Composable
    private fun RowDataInfo(label: String, data: String) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Text(
                label,
                style = text14_222,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Text(
                data,
                style = text14_62,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1.7f)
                    .padding(start = 20.dp)
            )
        }
    }

    @Composable
    private fun ProductList() {
        val listCompany = viewModel.companyDetail.value.products ?: mutableListOf()
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(listCompany) { _, obj ->
                ItemProduct(obj)
                Divider(color = ColorUtils.black_000000_opacity_5)
            }
        }
    }

    @Composable
    private fun ItemProduct(obj: ProductModel) {
        Row(Modifier.padding(16.dp)) {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${
                    obj.images?.getOrNull(0)?.url
                }",
                modifier = Modifier.size(98.dp),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja)
            )
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .height(98.dp)
                    .weight(1f)
            ) {
                Text(
                    "${obj.name?.getOrNull(0)?.name}",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$ ${obj.dollar}",
                    color = ColorUtils.gray_222222,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
                Text(
                    "₱ ${obj.peso}",
                    color = ColorUtils.gray_222222,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "${obj.description?.getOrNull(0)?.name}",
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { this@CompanyDetailFragment.userDetail1.value = userDetail }
            }
        }
    }

    @Composable
    private fun ButtonLike(
        text: String,
        icon: Int,
        number: Int,
        onClick: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .size(89.dp, 24.dp)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 7.dp, vertical = 5.dp)
                .noRippleClickable {
                    onClick.invoke()
                }
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text("$text $number", color = ColorUtils.black_000000, fontSize = 12.sp)
        }
    }
}