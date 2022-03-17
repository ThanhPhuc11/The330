package com.nagaja.the330.view.searchmain

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.ChooseKeyValue
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.companydetail.CompanyDetailFragment
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailFragment
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailFragment

class SearchMainFragment : BaseFragment() {
    private lateinit var viewModel: SearchMainVM

    companion object {
        fun newInstance() = SearchMainFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SearchMainVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        val listTitleSearch = GetDummyData.getTitleSearch().map {
            ChooseKeyValue(it.id, it.name, false)
        }.toMutableStateList()

        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        listTitleSearch[0].isSelected = true
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
            HeaderSearch(
                clickBack = { viewController?.popFragment() },
                clickSearch = {
                    viewModel.keyword = it.ifEmpty { null }
                }
            )
            Text(
                "검색 결과 총 0개",
                color = ColorUtils.black_000000,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                state = rememberLazyListState(),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(bottom = 5.dp)
            ) {
                itemsIndexed(listTitleSearch) { index, obj ->
                    ItemCategory(obj) {
                        listTitleSearch.onEach {
                            it.isSelected = false
                        }
                        val newObj = listTitleSearch[index].apply {
                            isSelected = true
                        }
                        listTitleSearch.removeAt(index)
                        listTitleSearch.add(index, newObj)

                        viewModel.stateCategoryType.value = obj.id!!
                    }
                }
            }
            //TODO: Content Search
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "‘우앵우앵’에 대한 검색 결과가 없습니다.",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                SwapScreen()
            }
        }
    }

    @Composable
    private fun SwapScreen() {
        when (viewModel.stateCategoryType.value) {
            AppConstants.SearchTitle.COMPANY -> {
                CompanyTab()
            }
            AppConstants.SearchTitle.FREE_NOTICE_BOARD -> {
                FreeNoticeTab()
            }
            AppConstants.SearchTitle.RECRUIT_JOBSEARCH -> {
                FreeNoticeTab()
            }
        }
    }

    @Composable
    private fun ItemCategory(obj: ChooseKeyValue, onClick: () -> Unit) {
        if (!obj.isSelected)
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .height(31.dp)
                    .border(width = 1.dp, color = ColorUtils.blue_2177E4_opacity_10)
                    .padding(horizontal = 10.dp)
                    .noRippleClickable {
                        onClick.invoke()
//                        onClickChoose?.invoke(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(obj.name ?: "", color = ColorUtils.gray_797979, fontSize = 12.sp)
            }
        else
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .height(31.dp)
                    .border(width = 1.dp, color = ColorUtils.blue_2177E4)
                    .padding(horizontal = 10.dp)
                    .noRippleClickable {
//                        onClickChoose?.invoke(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(obj.name ?: "", color = ColorUtils.blue_2177E4, fontSize = 12.sp)
            }
    }

    @Preview
    @Composable
    fun HeaderSearch(
        clickBack: (() -> Unit)? = null,
        clickSearch: ((String) -> Unit)? = null,
    ) {
        val stateEdtInput = remember { mutableStateOf(TextFieldValue("")) }
        Row(
            modifier = Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "",
                Modifier
                    .padding(horizontal = 19.dp)
                    .noRippleClickable { clickBack?.invoke() }
            )
            Row(
                Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
                    .height(36.dp)
                    .background(ColorUtils.blue_2177E4_opacity_5)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.blue_2177E4_opacity_10,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = stateEdtInput.value,
                    onValueChange = {
                        if (it.text.length <= 100) stateEdtInput.value = it
                    },
                    Modifier
                        .weight(1f),
                    singleLine = true,
                    keyboardActions = KeyboardActions(onSearch = {
                        clickSearch?.invoke(stateEdtInput.value.text)
                    }),
                    textStyle = TextStyle(
                        color = ColorUtils.black_000000
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    decorationBox = { innerTextField ->
                        Row {
                            if (stateEdtInput.value.text.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.please_enter_search_term),
                                    color = ColorUtils.gray_565656,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        innerTextField()
                    },
                )

                Image(
                    painter = painterResource(R.drawable.ic_close_round_gray),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .noRippleClickable {
                            stateEdtInput.value = TextFieldValue("")
                        }
                )
            }
        }
    }


    @Composable
    private fun CompanyTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let { viewModel.findCompany(it) }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        val listData = viewModel.stateListCompany
        LazyColumn(state = rememberLazyListState()) {
            itemsIndexed(listData) { index, obj ->
                ItemCompany(obj) {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_COMPANY_DETAIL,
                        CompanyDetailFragment.newInstance(obj.id!!)
                    )
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }

    @Composable
    private fun FreeNoticeTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            viewModel.getFreeNoticeBoard(it)
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

        val listData = viewModel.stateListFreeNotice
        LazyColumn(state = rememberLazyListState()) {
            itemsIndexed(listData) { index, obj ->
                if (obj.status == "DEACTIVATED") {
                    ItemDisable()
                } else {
                    ItemFreeNotice(obj) {
                        if (obj.notice != true) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_FREE_NOTICE_DETAIL,
                                FreeNoticeDetailFragment.newInstance(obj.id!!)
                            )
                        }
                    }
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }

    @Composable
    private fun RecruitJobsTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let { viewModel.getRecruitmentList(it) }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        val listData = viewModel.stateListRecruitJobs
        LazyColumn(state = rememberLazyListState()) {
            itemsIndexed(listData) { index, obj ->
                ItemRecuitmentJobs(obj) {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_DETAIL,
                        RecruitJobsDetailFragment.newInstance(obj.id!!)
                    )
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }
}