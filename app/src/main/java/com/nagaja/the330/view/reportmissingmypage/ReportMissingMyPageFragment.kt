package com.nagaja.the330.view.reportmissingmypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.ReportMissingModel
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.reportmissingdetail.ReportMissingDetailFragment
import com.nagaja.the330.view.reportmissingdetailmypage.ReportMissingDetailMyPageFragment
import com.nagaja.the330.view.text14_222

class ReportMissingMyPageFragment : BaseFragment() {
    private lateinit var viewModel: ReportMissingMyPageVM
    private var onClickSort: ((String) -> Unit)? = null

    companion object {
        fun newInstance() = ReportMissingMyPageFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ReportMissingMyPageVM::class.java]
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
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getReportMissingMyPage(accessToken!!, 0)
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
        LayoutTheme330 {
            Header(stringResource(R.string.report_missing_list)) {
                viewController?.popFragment()
            }
            Row(
                Modifier
                    .padding(horizontal = 9.dp)
                    .padding(top = 16.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                HandleSortUI()
            }

            Divider(color = ColorUtils.gray_E1E1E1)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                BoxStatus(Modifier.weight(1f), label = "신고/실종자", number = 30) {
                    viewModel.status = null
                    viewModel.getReportMissingMyPage(accessToken!!, 0)
                }
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                BoxStatus(Modifier.weight(1f), label = "등록완료", number = 30) {
                    viewModel.status = "REGISTRATION_COMPLETED"
                    viewModel.getReportMissingMyPage(accessToken!!, 0)
                }
                Box(
                    Modifier
                        .padding(vertical = 7.dp)
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                BoxStatus(Modifier.weight(1f), label = "등록불가", number = 10) {
                    viewModel.status = "REGISTRATION_IMPOSSIBLE"
                    viewModel.getReportMissingMyPage(accessToken!!, 0)
                }
            }
            Divider(color = ColorUtils.gray_E1E1E1)

            val listReportMissing = viewModel.stateListDataReport
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                itemsIndexed(listReportMissing) { _, obj ->
                    ReportMissingItem(obj)
                    Divider(color = ColorUtils.black_000000_opacity_5)
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getReportMissingMyPage(accessToken!!, page)
            }
        }
    }


    @Composable
    private fun BoxStatus(
        modifier: Modifier = Modifier,
        label: String,
        number: Int,
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
            Text("${number}건", style = text14_222)
        }
    }

    @Composable
    private fun HandleSortUI() {
        val listSort = remember {
            GetDummyData.getSortReservation(requireContext())
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
            onClickSort = { id ->
                viewModel.timeLimit = id
                viewModel.getReportMissingMyPage(accessToken!!, 0)
            }
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
                            onClickSort?.invoke(selectionOption.id ?: "null")
                            showMessDEBUG(itemSelected.value.id)
                        }
                    ) {
                        Text(text = selectionOption.name!!)
                    }
                }
            }
        }
    }

    @Composable
    private fun ReportMissingItem(obj: ReportMissingModel) {
        Column(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .noRippleClickable {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_REPORT_MISSING_DETAIL_MYPAGE,
                        ReportMissingDetailMyPageFragment.newInstance(obj.id!!)
                    )
                }
        ) {
            Text("{YYYY/MM/DD(게시일)}", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp)
            Text(
                "${obj.title}",
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_15,
                        obj.createdOn ?: ""
                    ).plus("(처리일)"),
                    color = ColorUtils.gray_222222,
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
                    "inprocessing",
                    color = ColorUtils.gray_222222,
                    fontSize = 12.sp,
                )
            }
        }
    }
}