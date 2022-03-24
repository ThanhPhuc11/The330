package com.nagaja.the330.view.recruimentcompany

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.model.RoomDetailModel
import com.nagaja.the330.utils.*
import com.nagaja.the330.view.*
import com.nagaja.the330.view.chatdetail.ChatDetailFragment
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecruitmentCompanyFragment : BaseFragment() {
    private lateinit var viewModel: RecruitmentCompanyVM

    companion object {
        @JvmStatic
        fun newInstance() = RecruitmentCompanyFragment(

        )
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[RecruitmentCompanyVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @OptIn(ExperimentalPagerApi::class)
    @Preview
    @Composable
    override fun UIData() {
        val pagerState = rememberPagerState(pageCount = 2)
        LayoutTheme330 {
            Header(stringResource(R.string.option_regular_list)) {
                viewController?.popFragment()
            }
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(width = 1.dp, color = ColorUtils.gray_222222)
            ) {
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.secondhand_list),
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.consultation_list),
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

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                RecruitmentList()
            } else {
                ConsultationList()
            }
        }
    }

    @Composable
    private fun RecruitmentList() {
        val owner = LocalLifecycleOwner.current
        val dataSort = GetDummyData.getSortReservationRoleCompany(context = requireContext())
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.timeLimit = dataSort[0].id!!
                        accessToken?.let {
                            viewModel.getRecruitmentMypage(it, 0)
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

        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp)
                    .height(IntrinsicSize.Max)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable {
                            Toast
                                .makeText(requireContext(), "click", Toast.LENGTH_LONG)
                                .show()
                        },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "상담중 ${viewModel.totalCase.value}건",
                        color = ColorUtils.black_000000,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    HandleSortUI(
                        context = requireContext(),
                        dataSort
                    ) {
                        viewModel.timeLimit = it.id!!
                        viewModel.getRecruitmentMypage(accessToken!!, 0)
                    }
                }
            }
            Divider(
                color = ColorUtils.gray_E1E1E1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            val listData = viewModel.stateListRecuitment
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
                itemsIndexed(listData) { _, obj ->
                    ItemSingleColumn(obj) {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_DETAIL,
                            RecruitJobsDetailFragment.newInstance(obj.id!!)
                        )
                    }
                }
            }
            LoadmoreHandler(lazyListState) { page ->
                viewModel.getRecruitmentMypage(accessToken!!, page)
            }
        }
    }

    @Composable
    fun ItemSingleColumn(
        item: RecruitmentJobsModel,
        onClick: () -> Unit
    ) {
        Column(
            Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .height(IntrinsicSize.Max)
                .clickable {
                    onClick.invoke()
                }
        ) {
            Text(
                text = "${item.title}",
                modifier = Modifier.fillMaxWidth(),
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = AppDateUtils.changeDateFormat(
                    AppDateUtils.FORMAT_16,
                    AppDateUtils.FORMAT_23,
                    item.createdOn ?: ""
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${item.body}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 16.dp),
                color = ColorUtils.gray_626262,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .background(ColorUtils.gray_E1E1E1),
            )
        }
    }

    @Composable
    private fun ConsultationList() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getChatList(accessToken!!, 0)
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
            val listData = viewModel.stateListRoom
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
                itemsIndexed(listData) { _, obj ->
                    ConsultationItem(obj) {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_CHAT_DETAIL,
                            ChatDetailFragment.newInstance(
                                roomId = obj.id,
                                type = AppConstants.SECONDHAND
                            )
                        )
                    }
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getChatList(accessToken!!, page)
            }
        }
    }

    @Composable
    private fun ConsultationItem(obj: RoomDetailModel, onClick: () -> Unit) {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .noRippleClickable {
                    onClick()
                }
        ) {
            Text(
                "${obj.recruitmentJob?.title}",
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                "${obj.target?.name}",
                modifier = Modifier.padding(top = 5.dp),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp
            )
            Text(
                "${obj.lastMessage?.message}",
                modifier = Modifier.padding(top = 5.dp),
                style = text14_62,
                textAlign = TextAlign.Start
            )
            Text(
                "{${
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_7,
                        AppDateUtils.FORMAT_23,
                        obj.createdOn ?: ""
                    )
                }(최초 대화일시)}\n" +
                        "{${
                            AppDateUtils.changeDateFormat(
                                AppDateUtils.FORMAT_7,
                                AppDateUtils.FORMAT_23,
                                obj.createdOn ?: ""
                            )
                        }(최종 대화일시)}",
                modifier = Modifier.padding(top = 5.dp),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorUtils.gray_E1E1E1)
            )
        }
    }
}