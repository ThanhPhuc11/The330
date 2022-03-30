package com.nagaja.the330.view.point

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PointFragment : BaseFragment() {
    private lateinit var viewModel: PointVM

    companion object {
        fun newInstance(): PointFragment = PointFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[PointVM::class.java]
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
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getCompanyDetail(
                            accessToken!!,
                            userDetailBase?.companyRequest?.id ?: 0
                        )
                        backSystemHandler {
                            viewController?.popFragment()
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {}
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
            Header(stringResource(R.string.point_status)) {
                viewController?.popFragment()
            }

            Row(
                Modifier
                    .padding(horizontal = 9.dp)
                    .padding(top = 16.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    Modifier
                        .background(ColorUtils.gray_222222, RoundedCornerShape(4.dp))
                        .padding(7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("전체 내역 다운로드", color = ColorUtils.white_FFFFFF, fontSize = 12.sp)
                }
            }

            Divider(color = ColorUtils.gray_E1E1E1)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                BoxStatus(
                    Modifier.weight(1f),
                    label = "충전",
                    point = viewModel.companyDetail.value.pointCharge ?: 0,
                    number = viewModel.chargeCount.value
                ) {

                }
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                BoxStatus(
                    Modifier.weight(1f),
                    label = "사용",
                    point = viewModel.companyDetail.value.pointUse ?: 0,
                    number = viewModel.usageCount.value
                ) {

                }
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                BoxStatus(
                    Modifier.weight(1f),
                    label = "잔여",
                    point = viewModel.companyDetail.value.pointRemain ?: 0,
                    number = null
                ) {

                }
            }
            Divider(color = ColorUtils.gray_E1E1E1)

            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(width = 1.dp, color = ColorUtils.gray_222222)
            ) {
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.charging_history),
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.usage_history),
                    isSelected = pagerState.currentPage == 1
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(1)
                    }
                }
            }
            SetupPager(pagerState)
        }
    }

    @Composable
    private fun BoxStatus(
        modifier: Modifier = Modifier,
        label: String,
        point: Int,
        number: Int? = null,
        onClick: () -> Unit
    ) {
        Column(
            modifier
                .fillMaxHeight()
                .noRippleClickable {
                    onClick()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = text14_222)
            val count = if (number == null) "" else "(${number}건)"
            Text("${point}P${count}", style = text14_222)
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
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = ColorUtils.gray_222222, fontSize = 16.sp)
            }
        else
            Box(
                modifier
                    .background(ColorUtils.gray_222222)
                    .fillMaxHeight()
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 16.sp,
                    modifier = Modifier.alpha(0.4f)
                )
            }

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                ChargingHistoryTab()
            } else {
                UsageHistoryTab()
            }
        }
    }

    @Composable
    private fun ChargingHistoryTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
//                        viewModel.type = AppConstants.RECRUITMENT
                        viewModel.getPoints(accessToken!!, 0, AppConstants.Point.PROVIDED)
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(AppConstants.Point.PROVIDED)
            }
            Divider(color = ColorUtils.white_FFFFFF, thickness = 10.dp)
            ItemCharge(
                stringResource(R.string.charge_date),
                stringResource(R.string.charge_type_service_payment),
                stringResource(R.string.charge_point)
            )
            Divider(color = ColorUtils.gray_E1E1E1)
            val listData = viewModel.stateListCharge
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
                itemsIndexed(listData) { index, obj ->
                    ItemCharge(
                        date = AppDateUtils.changeDateFormat(
                            AppDateUtils.FORMAT_16,
                            AppDateUtils.FORMAT_21,
                            obj.createdOn ?: ""
                        ),
                        type = "${obj.pointProvidedType}",
                        point = "+${obj.amount ?: 0} P"
                    )
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getPoints(accessToken!!, page, AppConstants.Point.PROVIDED)
            }
        }
    }

    @Composable
    private fun UsageHistoryTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
//                        viewModel.type = AppConstants.RECRUITMENT
                        viewModel.getPoints(accessToken!!, 0, AppConstants.Point.USAGE)
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(AppConstants.Point.USAGE)
            }
            Divider(color = ColorUtils.white_FFFFFF, thickness = 10.dp)
            ItemCharge(
                stringResource(R.string.usage_date),
                stringResource(R.string.usage_reason),
                stringResource(R.string.usage_point)
            )
            Divider(color = ColorUtils.gray_E1E1E1)
            val listData = viewModel.stateListUsage
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
                itemsIndexed(listData) { index, obj ->
                    ItemCharge(
                        date = AppDateUtils.changeDateFormat(
                            AppDateUtils.FORMAT_16,
                            AppDateUtils.FORMAT_21,
                            obj.createdOn ?: ""
                        ),
                        type = "${obj.reason}",
                        point = "-${obj.amount ?: 0} P"
                    )
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getPoints(accessToken!!, page, AppConstants.Point.PROVIDED)
            }
        }
    }

    @Composable
    private fun HandleSortUI(pointTransactionType: String) {
        val listSort = remember {
            GetDummyData.getSortReservationRoleCompany(requireContext())
        }
        var expanded by remember { mutableStateOf(false) }
        val itemSelected = remember { mutableStateOf(listSort[0]) }
        Row(
            Modifier
                .size(100.dp, 36.dp)
                .border(width = 1.dp, color = ColorUtils.gray_E1E1E1)
                .padding(9.dp)
                .noRippleClickable {
                    expanded = true
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
//            onClickSort = { id ->
//                viewModel.timeLimit = id
//                viewModel.getReportMissingMyPage(accessToken!!)
//            }
            Text(
                itemSelected.value.name ?: "",
                style = text14_222,
                modifier = Modifier.weight(1f)
            )
            Image(
                painterResource(R.drawable.ic_arrow_down), null,
                Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .width(10.dp),
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
                            viewModel.getPoints(accessToken!!, 0, pointTransactionType)
                            showMessDEBUG(itemSelected.value.id)
                        }
                    ) {
                        Text(text = selectionOption.name!!)
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun ItemCharge(
        date: String = "YYYY/MM/DD\nHH:MM",
        type: String = "관리자 아이디 추가",
        point: String = "+ 99,999 P"
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    date,
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(type, color = ColorUtils.black_000000, fontSize = 13.sp)
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(point, color = ColorUtils.black_000000, fontSize = 13.sp)
            }
        }
    }

//    @Preview
//    @Composable
//    private fun ItemUsage(
//        date: String = "YYYY/MM/DD\nHH:MM",
//        type: String = "reason",
//        point: String = "- 99,999 P"
//    ) {
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(vertical = 10.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
//                Text(date, color = ColorUtils.black_000000, fontSize = 13.sp)
//            }
//            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
//                Text(type, color = ColorUtils.black_000000, fontSize = 13.sp)
//            }
//            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
//                Text(point, color = ColorUtils.black_000000, fontSize = 13.sp)
//            }
//        }
//    }
}