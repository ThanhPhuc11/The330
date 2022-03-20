package com.nagaja.the330.view.reservationcompany

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.ReservationModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_62
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


//private lateinit var viewModel: ReservationVM

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ReservationCompanyScreen(accessToken: String, viewController: ViewController?) {
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
    )[ReservationCompanyVM::class.java]

    viewModel.accessToken = accessToken
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    getUserDetailFromDataStore(context) {
                        viewModel.reservationOverview(accessToken, it)
                    }

//                    viewModel.callbackStart.observe(owner) {
//                        showLoading()
//                    }
//                    viewModel.callbackSuccess.observe(viewLifecycleOwner) {
//                        hideLoading()
//                    }
//                    viewModel.callbackFail.observe(viewLifecycleOwner) {
//                        hideLoading()
//                    }
                    viewModel.showMessCallback.observe(owner) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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
        Header(stringResource(R.string.option_reservation_status)) {
//            viewController?.popFragment()
        }
        //TODO: TabSelect
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(40.dp)
                .border(width = 1.dp, color = ColorUtils.gray_222222)
        ) {
            LaunchedEffect(pagerState) {
                if (pagerState.currentPage == 0) {

                } else {

                }
            }
            TabSelected(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.company_reservation_list),
                isSelected = pagerState.currentPage == 0
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    pagerState.scrollToPage(0)
                }
            }
            TabSelected(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.my_reservation_list),
                isSelected = pagerState.currentPage == 1
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    pagerState.scrollToPage(1)
                }
            }
        }
        //TODO: horizontal pager
        SetupPager(pagerState = pagerState, modifier = Modifier.weight(1f), viewModel, accessToken)
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
        Box(
            modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxHeight()
                .border(width = 1.dp, color = ColorUtils.gray_222222)
                .noRippleClickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = ColorUtils.gray_222222, fontSize = 14.sp)
        }
    else
        Box(
            modifier
                .background(ColorUtils.gray_222222)
                .fillMaxHeight()
                .border(width = 1.dp, color = ColorUtils.gray_222222)
                .noRippleClickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = ColorUtils.white_FFFFFF,
                fontSize = 14.sp
            )
        }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun SetupPager(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    viewModel: ReservationCompanyVM,
    token: String
) {
    Column(modifier) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            if (page == 0) {
                Tab1(viewModel, token)
            } else {
                Tab2(viewModel, token)
            }
        }
    }
}

@Composable
private fun Tab1(viewModel: ReservationCompanyVM, token: String) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
//                    getUserDetailFromDataStore(context) {
//                        viewModel.reservationOverview(accessToken, it)
//                    }
                    viewModel.getReservationCompany(viewModel.accessToken, 0)
                }
                else -> {}
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
    Column(Modifier.fillMaxSize()) {
        Divider(color = ColorUtils.gray_E1E1E1)
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxStatus(Modifier.weight(1f), text = "총 예약\n${viewModel.total.value}건") {
                viewModel.status = null
                viewModel.getReservationCompany(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(
                Modifier.weight(1f),
                text = "예약완료\n${viewModel.reservation_completed.value}건"
            ) {
                viewModel.status = AppConstants.Reservation.RESERVATION_COMPLETED
                viewModel.getReservationCompany(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(Modifier.weight(1f), text = "이용완료\n${viewModel.usage_completed.value}건") {
                viewModel.status = AppConstants.Reservation.USAGE_COMPLETED
                viewModel.getReservationCompany(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(Modifier.weight(1f), text = "예약취소\n${viewModel.reservation_cancel.value}건") {
                viewModel.status = AppConstants.Reservation.RESERVATION_CANCELED
                viewModel.getReservationCompany(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )
        }
        Divider(color = ColorUtils.gray_E1E1E1)

        Row(
            Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .size(98.dp, 36.dp)
                    .background(ColorUtils.blue_2177E4, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("오늘 예약 마감", color = ColorUtils.white_FFFFFF, fontSize = 14.sp)
            }
            Spacer(Modifier.weight(1f))

            val listSort = remember {
                GetDummyData.getSortReservationRoleCompany(context)
            }
            var expanded by remember { mutableStateOf(false) }
            val itemSelected = remember { mutableStateOf(listSort[0]) }
            Row(
                Modifier
                    .size(98.dp, 36.dp)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1
                    )
                    .padding(horizontal = 9.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${itemSelected.value.name}",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painterResource(R.drawable.ic_arrow_down), null, modifier = Modifier
                        .rotate(if (expanded) 180f else 0f)
                        .width(10.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    listSort.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                itemSelected.value = selectionOption
                                expanded = false
                                viewModel.timeLimit = selectionOption.id!!
                                viewModel.getReservationCompany(viewModel.accessToken, 0)
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }
        }
        val listData = viewModel.stateListDataCompany
        val lazyListState = rememberLazyListState()
        LazyColumn(state = lazyListState) {
            itemsIndexed(listData) { index, obj ->
                ItemReservationTab1(index, obj,
                    onCompleted = {
                        viewModel.editReservation(token, obj.id!!, "USAGE_COMPLETED")
                    },
                    onCancel = {
                        viewModel.editReservation(token, obj.id!!, "RESERVATION_CANCELED")
                    })
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }

        LoadmoreHandler(lazyListState) { page ->
            viewModel.getReservationCompany(token, page)
        }
    }

}

@Composable
private fun ItemReservationTab1(
    index: Int,
    obj: ReservationModel,
    onCompleted: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onReport: (() -> Unit)? = null
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            "${obj.bookerName}",
            color = ColorUtils.gray_222222,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "예약일시: ${
                AppDateUtils.changeDateFormat(
                    AppDateUtils.FORMAT_16,
                    AppDateUtils.FORMAT_20,
                    obj.reservationDateTime ?: ""
                )
            }",
            color = ColorUtils.gray_222222,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            "사용인원: ${obj.reservationNumber ?: 0}인",
            style = text14_62
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (obj.status == AppConstants.Reservation.RESERVATION_COMPLETED) {
                ButtonStatus(
                    "서비스완료",
                    ColorUtils.blue_2177E4,
                    ColorUtils.white_FFFFFF,
                    ColorUtils.blue_2177E4
                ) {
                    onCompleted?.invoke()
                }

                ButtonStatus(
                    "예약취소",
                    ColorUtils.gray_222222,
                    ColorUtils.white_FFFFFF,
                    ColorUtils.gray_222222
                ) {
                    onCancel?.invoke()
                }
            }

            if (obj.autoCancel == true) {
                ButtonStatus(
                    "사용자신고",
                    ColorUtils.white_FFFFFF,
                    ColorUtils.gray_222222,
                    ColorUtils.gray_222222
                ) {
                    onReport?.invoke()
                }
            }
        }
    }
}

@Composable
private fun ButtonStatus(
    text: String,
    textColor: Color,
    background: Color,
    border: Color,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .padding(start = 8.dp)
            .size(76.dp, 32.dp)
            .background(background, RoundedCornerShape(99.dp))
            .border(
                width = 1.dp,
                color = border,
                shape = RoundedCornerShape(99.dp)
            )
            .noRippleClickable {
                onClick.invoke()
            }, contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 12.sp)
    }
}

@Composable
private fun Tab2(viewModel: ReservationCompanyVM, token: String) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
//                    getUserDetailFromDataStore(context) {
//                        viewModel.reservationOverview(accessToken, it)
//                    }
                    viewModel.getReservationGeneral(viewModel.accessToken, 0)
                }
                else -> {}
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Divider(color = ColorUtils.gray_E1E1E1)
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxStatus(Modifier.weight(1f), text = "총 예약\n${viewModel.total.value}건") {
                viewModel.status = null
                viewModel.getReservationGeneral(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(
                Modifier.weight(1f),
                text = "예약완료\n${viewModel.reservation_completed.value}건"
            ) {
                viewModel.status = AppConstants.Reservation.RESERVATION_COMPLETED
                viewModel.getReservationGeneral(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(Modifier.weight(1f), text = "이용완료\n${viewModel.usage_completed.value}건") {
                viewModel.status = AppConstants.Reservation.USAGE_COMPLETED
                viewModel.getReservationGeneral(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )

            BoxStatus(Modifier.weight(1f), text = "예약취소\n${viewModel.reservation_cancel.value}건") {
                viewModel.status = AppConstants.Reservation.RESERVATION_CANCELED
                viewModel.getReservationGeneral(token, 0)
            }
            Box(
                Modifier
                    .padding(vertical = 7.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorUtils.gray_E1E1E1)
            )
        }
        Divider(color = ColorUtils.gray_E1E1E1)

        Row(
            Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))

            val listSort = remember {
                GetDummyData.getSortReservationRoleCompany(context)
            }
            var expanded by remember { mutableStateOf(false) }
            val itemSelected = remember { mutableStateOf(listSort[0]) }
            Row(
                Modifier
                    .size(98.dp, 36.dp)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1
                    )
                    .padding(horizontal = 9.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${itemSelected.value.name}",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painterResource(R.drawable.ic_arrow_down), null, modifier = Modifier
                        .rotate(if (expanded) 180f else 0f)
                        .width(10.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    listSort.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                itemSelected.value = selectionOption
                                expanded = false
                                viewModel.timeLimit = selectionOption.id!!
                                viewModel.getReservationGeneral(viewModel.accessToken, 0)
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }
        }

        val listData = viewModel.stateListDataGeneral
        val lazyListState = rememberLazyListState()
        LazyColumn(state = lazyListState) {
            itemsIndexed(listData) { index, obj ->
                ItemReservationTab2(index, obj)
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }

        LoadmoreHandler(lazyListState) { page ->
            viewModel.getReservationGeneral(token, page)
        }
    }

}

@Composable
private fun BoxStatus(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Box(
        modifier
            .fillMaxHeight()
            .noRippleClickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = ColorUtils.black_000000,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ItemReservationTab2(index: Int, obj: ReservationModel) {
    Row(
        Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        GlideImage(
            imageModel = "${BuildConfig.BASE_S3}${obj.companyOwner?.images?.getOrNull(0)?.url}",
            Modifier.size(96.dp),
            placeHolder = painterResource(R.drawable.ic_default_nagaja),
            error = painterResource(R.drawable.ic_default_nagaja)
        )
        Column(Modifier.padding(start = 9.dp)) {
            Text(
                obj.companyOwner?.name?.getOrNull(0)?.name ?: "",
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                "예약일시: ${
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_20,
                        obj.reservationDateTime ?: ""
                    )
                }",
                modifier = Modifier.padding(top = 3.dp),
                style = text14_62
            )
            Text(
                "사용인원: ${obj.reservationNumber ?: 0}인",
                modifier = Modifier.padding(top = 3.dp),
                style = text14_62
            )
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    Modifier
                        .width(76.dp)
                        .height(32.dp)
                        .background(ColorUtils.gray_222222, shape = RoundedCornerShape(99.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("예약취소", color = ColorUtils.white_FFFFFF, fontSize = 12.sp)
                }
            }
        }
    }
}

private fun getUserDetailFromDataStore(context: Context, onClick: (Int) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.data.map { get ->
            get[DataStorePref.USER_DETAIL] ?: ""
        }.collect {
            val userDetail = Gson().fromJson(it, UserDetail::class.java)
            userDetail?.id?.let { id ->
                onClick(id)
            }
        }
    }
}
