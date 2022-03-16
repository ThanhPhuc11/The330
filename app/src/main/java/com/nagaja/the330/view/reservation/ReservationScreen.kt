package com.nagaja.the330.view.reservation

import android.content.Context
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
import androidx.compose.ui.graphics.ColorFilter
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
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.ReservationModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
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

@Composable
fun ReservationScreen(accessToken: String, viewController: ViewController?) {
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
    )[ReservationVM::class.java]

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    getUserDetailFromDataStore(context) {
                        viewModel.reservationOverview(accessToken, it)
                    }
                    viewModel.getReservationMain(accessToken)
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
        Header(stringResource(R.string.option_reservation_status)) {
//            viewController?.popFragment()
        }
        Box(
            Modifier
                .padding(horizontal = 9.dp, vertical = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            val listSort = remember { GetDummyData.getSortReservation(context) }
            var expanded by remember { mutableStateOf(false) }
            val itemSelected = remember { mutableStateOf(KeyValueModel()) }
            LaunchedEffect(listSort.size) {
                itemSelected.value =
                    if (listSort.size > 0) listSort[0] else KeyValueModel()
            }
            Row(
                Modifier
                    .size(100.dp, 36.dp)
                    .border(width = 1.dp, color = ColorUtils.gray_E1E1E1)
                    .padding(9.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    itemSelected.value.name ?: "",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ColorUtils.black_000000),
                    modifier = Modifier.size(10.dp, 6.dp)
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
                                viewModel.getReservationMain(accessToken)
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }
        }
        Divider(color = ColorUtils.gray_E1E1E1)
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxStatus(Modifier.weight(1f), text = "총 예약\n${viewModel.total.value}건") {
                viewModel.status = null
                viewModel.getReservationMain(accessToken)
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
                viewModel.getReservationMain(accessToken)
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
                viewModel.getReservationMain(accessToken)
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
                viewModel.getReservationMain(accessToken)
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

        val listData = viewModel.stateListData
        LazyColumn(state = rememberLazyListState()) {
            itemsIndexed(listData) { index, obj ->
                ItemReservation(index, obj) {
                    viewModel.cancelReservation(accessToken, it)
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
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
private fun ItemReservation(index: Int, obj: ReservationModel, onClickCancel: (Int) -> Unit) {
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
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    Modifier
                        .width(76.dp)
                        .height(32.dp)
                        .background(ColorUtils.gray_222222, shape = RoundedCornerShape(99.dp))
                        .noRippleClickable {
                            if (obj.status == "RESERVATION_COMPLETED")
                                onClickCancel.invoke(obj.id!!)
                        },
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
