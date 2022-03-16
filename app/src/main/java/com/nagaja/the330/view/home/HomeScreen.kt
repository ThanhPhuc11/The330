package com.nagaja.the330.view.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CategoryModel
import com.nagaja.the330.model.CompanyRecommendModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.companylist.CompanyListFragment
import com.nagaja.the330.view.event.OnGoingEventsFragment
import com.nagaja.the330.view.faq.FAQsFragment
import com.nagaja.the330.view.freenoticeboard.FreeNoticeFragment
import com.nagaja.the330.view.localnews.LocalNewsFragment
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.notification.NotificationsFragment
import com.nagaja.the330.view.recruitment.RecruitmentJobSearchFragment
import com.nagaja.the330.view.reportmissing.ReportMissingFragment
import com.nagaja.the330.view.secondhandmarket.SecondHandMarketFragment
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(accessToken: String, viewController: ViewController?) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
    val viewModel = ViewModelProvider(
        viewModelStoreOwner,
        ViewModelFactory(
            RetrofitBuilder.getInstance(context)
                ?.create(ApiService::class.java)!!
        )
    )[HomeScreenVM::class.java]

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.getCategory(accessToken, null)
                    viewModel.getCompanyRecommendAds(accessToken)
                }
                Lifecycle.Event.ON_STOP -> {

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
        LogoAndSearch()
        SearchFilter()
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            CategoryMain(viewModel, viewController)
            ExchangeDollar()
            BannerEvent(viewController = viewController)
            Row(
                Modifier
                    .padding(top = 22.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_png),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(ColorUtils.blue_2177E4),
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    text = "추천 업체",
                    modifier = Modifier.padding(start = 8.dp),
                    color = ColorUtils.black_000000,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black
                )
            }
            ListCompanyRecommended(viewModel)
        }

    }
}

@Composable
private fun LogoAndSearch() {
    Row(
        Modifier
            .padding(top = 7.dp, bottom = 3.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_png),
            contentDescription = "",
            colorFilter = ColorFilter.tint(ColorUtils.blue_2177E4),
            modifier = Modifier.height(18.dp)
        )
        Row(
            Modifier
                .padding(start = 18.dp)
                .height(32.dp)
                .weight(1f)
//                .clip(RoundedCornerShape(4.dp))
                .background(ColorUtils.blue_2177E4_opacity_5, RoundedCornerShape(4.dp))
                .border(1.dp, ColorUtils.blue_2177E4_opacity_10, RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "",
                Modifier.padding(horizontal = 10.dp)
            )
            Text(
                "검색어를 입력해 보세요.",
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SearchFilter() {
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(modifier = Modifier.weight(1f), options = GetDummyData.getCoutryAdrressSignup())
    }
}

@Composable
private fun BoxSearch(modifier: Modifier = Modifier, options: MutableList<KeyValueModel>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    Row(
        modifier = modifier
            .noRippleClickable {
                expanded = !expanded
            }
            .border(
                width = 1.dp,
                color = ColorUtils.blue_2177E4_opacity_10,
                shape = RoundedCornerShape(4.dp)
            )
            .height(44.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            selectedOptionText.name!!,
            style = text14_222,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow_filter),
            contentDescription = "",
            Modifier.rotate(if (expanded) 180f else 0f)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.name!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoryMain(homeVM: HomeScreenVM, viewController: ViewController?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(0.dp, 1000.dp)
            .wrapContentHeight()
    ) {
        LazyVerticalGrid(
//        state = rememberLazyListState(),
            cells = GridCells.Fixed(5),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(homeVM.listCategoryState.value) { obj ->
                IconCategory(obj) {
                    Log.e("Category Type", it)
                    linkScreen(it, viewController)
                }
            }
        }
    }

}

fun linkScreen(ctype: String, viewController: ViewController?) {
    when (ctype) {
        "SECOND_HAND_MARKET" -> viewController?.pushFragment(
            ScreenId.SCREEN_SECONDHAND_MARKET,
            SecondHandMarketFragment.newInstance()
        )
        "LOCAL_NEWS" -> viewController?.pushFragment(
            ScreenId.SCREEN_LOCALNEWS_LIST,
            LocalNewsFragment.newInstance()
        )
        "FREE_BOARD" -> viewController?.pushFragment(
            ScreenId.SCREEN_FREE_NOTICE_BOARD,
            FreeNoticeFragment.newInstance()
        )
        "REPORT_MISSING_PERSON" -> viewController?.pushFragment(
            ScreenId.SCREEN_REPORT_MISSING,
            ReportMissingFragment.newInstance()
        )
        "JOB_SEARCH" -> viewController?.pushFragment(
            ScreenId.SCREEN_RECRUITMENT_JOBSEARCH,
            RecruitmentJobSearchFragment.newInstance()
        )
        "NOTICE" -> viewController?.pushFragment(
            ScreenId.SCREEN_NOTIFICATION,
            NotificationsFragment.newInstance()
        )
        "FAQ" -> viewController?.pushFragment(
            ScreenId.SCREEN_FQA,
            FAQsFragment.newInstance()
        )
        else -> viewController?.pushFragment(
            ScreenId.SCREEN_COMPANY_LIST,
            CompanyListFragment.newInstance(ctype)
        )
    }
}

@Composable
fun IconCategory(obj: CategoryModel, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
            .noRippleClickable {
                obj.ctype?.let { onClick.invoke(it) }
            }
    ) {
        GlideImage(
            imageModel = "${BuildConfig.BASE_S3}${obj.image ?: ""}",
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Fit,
//            circularReveal = CircularReveal(duration = 250),
            placeHolder = painterResource(R.drawable.ic_default_nagaja),
            error = painterResource(R.drawable.ic_default_nagaja),
            modifier = Modifier.size(36.dp)
        )
        Text(
            obj.name ?: "",
            fontSize = 12.sp,
            color = ColorUtils.gray_222222,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(top = 10.dp),
            maxLines = 2
        )
    }
}

@Preview
@Composable
private fun ExchangeDollar() {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .fillMaxWidth()
            .height(32.dp)
            .clip(
                shape = RoundedCornerShape(16.dp)
            )
            .background(ColorUtils.blue_2177E4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "오늘의 환율",
            fontSize = 12.sp,
            color = ColorUtils.white_FFFFFF,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        MoneyValue(nation = "USD", value = "1", symbol = "$", modifier = Modifier.weight(1f))
        MoneyValue(nation = "KRW", value = "1,200", symbol = "￦", modifier = Modifier.weight(1f))
        MoneyValue(nation = "PHP", value = "49.87", symbol = "₱", modifier = Modifier.weight(1f))
    }
}


@Composable
private fun MoneyValue(
    modifier: Modifier = Modifier,
    nation: String,
    value: String,
    symbol: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "$nation ",
            fontSize = 12.sp,
            color = ColorUtils.white_FFFFFF,
        )
        Text(
            value,
            fontSize = 12.sp,
            color = ColorUtils.white_FFFFFF,
            fontWeight = FontWeight.Bold
        )
        Text(
            symbol,
            fontSize = 12.sp,
            color = ColorUtils.white_FFFFFF,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun BannerEvent(viewController: ViewController?) {
    Image(
        painter = painterResource(R.drawable.banner_ads),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .noRippleClickable {
                viewController?.pushFragment(
                    ScreenId.SCREEN_ON_GOING_EVENTS,
                    OnGoingEventsFragment.newInstance()
                )
            }
    )
}

@Composable
private fun ListCompanyRecommended(viewModel: HomeScreenVM) {
    val listData = viewModel.statelistCompany
    LazyRow(state = rememberLazyListState()) {
        items(listData) { obj ->
            CompanyItemView(obj)
        }
    }
}

@Composable
private fun CompanyItemView(obj: CompanyRecommendModel) {
    Column(
        Modifier
            .padding(bottom = 6.dp, start = 16.dp, end = 4.dp)
            .background(ColorUtils.white_FFFFFF)
    ) {
        Box(
            Modifier
                .padding(bottom = 6.dp)
                .size(240.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.BottomStart
        ) {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${obj.companyRequest?.images?.getOrNull(0)?.url}",
                contentScale = ContentScale.Fit,
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
                modifier = Modifier.size(240.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_cover),
                contentDescription = null,
                modifier = Modifier.size(240.dp)
            )
            Text(
                "${obj.companyRequest?.name?.getOrNull(0)?.name}",
                color = ColorUtils.white_FFFFFF,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )
        }
        Text("주소: ${obj.companyRequest?.address}", color = ColorUtils.gray_222222, fontSize = 13.sp)
        Text("영업시간: am10:00~pm11:00", color = ColorUtils.gray_222222, fontSize = 13.sp)
        Text(
            "픽업/드랍: 불가능/가능",
            color = ColorUtils.gray_767676,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 7.dp)
        )
        Text("배달여부: 가능, 12:00~18:00", color = ColorUtils.gray_767676, fontSize = 13.sp)
        Text("예약여부: 가능, 12:00~18:00, 최대 5인", color = ColorUtils.gray_767676, fontSize = 13.sp)

        Row(Modifier.padding(top = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(R.drawable.ic_loa), contentDescription = null)
            Text(
                "${obj.ads}",
                color = ColorUtils.blue_2177E4,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}