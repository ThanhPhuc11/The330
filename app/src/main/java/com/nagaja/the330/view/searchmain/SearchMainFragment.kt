package com.nagaja.the330.view.searchmain

import android.os.Bundle
import androidx.compose.foundation.*
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
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
import com.nagaja.the330.model.ChooseKeyValue
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.companydetail.CompanyDetailFragment
import com.nagaja.the330.view.companylist.CompanyListVM
import com.nagaja.the330.view.freenoticeboard.FreeNoticeVM
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailFragment
import com.nagaja.the330.view.recruitment.RecruitmentJobSearchVM
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailFragment
import com.nagaja.the330.view.reportmissing.ReportMissingVM
import com.nagaja.the330.view.reportmissingdetail.ReportMissingDetailFragment
import com.nagaja.the330.view.secondhanddetail.SecondHandDetailFragment
import com.nagaja.the330.view.secondhandmarket.SecondHandMarketVM
import com.nagaja.the330.view.secondhandregis.SecondHandRegisFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchMainFragment : BaseFragment() {
    private lateinit var viewModel: SearchMainVM
    private lateinit var viewModelCompany: CompanyListVM
    private lateinit var viewModelFreeNotice: FreeNoticeVM
    private lateinit var viewModelRecruitJobs: RecruitmentJobSearchVM
    private lateinit var viewModelSecondhand: SecondHandMarketVM
    private lateinit var viewModelReportMissing: ReportMissingVM

    companion object {
        fun newInstance(keyword: String) = SearchMainFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.EXTRA_KEY1, keyword)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SearchMainVM::class.java]
        viewModelCompany = getViewModelProvider(this)[CompanyListVM::class.java]
        viewModelFreeNotice = getViewModelProvider(this)[FreeNoticeVM::class.java]
        viewModelRecruitJobs = getViewModelProvider(this)[RecruitmentJobSearchVM::class.java]
        viewModelSecondhand = getViewModelProvider(this)[SecondHandMarketVM::class.java]
        viewModelReportMissing = getViewModelProvider(this)[ReportMissingVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @OptIn(ExperimentalPagerApi::class)
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
                        viewModel.keyword.value = requireArguments().getString(AppConstants.EXTRA_KEY1, "")
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
                    viewModel.keyword.value = it.ifEmpty { null }
                },
                init = TextFieldValue(requireArguments().getString(AppConstants.EXTRA_KEY1, ""))
            )
            Text(
                "검색 결과 총 0개",
                color = ColorUtils.black_000000,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp)
            )
            val pagerState = rememberPagerState(pageCount = 5)
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
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.scrollToPage(index)
                        }
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
//                Text(
//                    "‘우앵우앵’에 대한 검색 결과가 없습니다.",
//                    color = ColorUtils.black_000000,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold
//                )
                LaunchedEffect(pagerState.currentPage) {
                    listTitleSearch.onEach {
                        it.isSelected = false
                    }
                    listTitleSearch[pagerState.currentPage] = listTitleSearch[pagerState.currentPage].copy(isSelected = true)
                }
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> {
                            CompanyTab()
                        }
                        1 -> {
                            FreeNoticeTab()
                        }
                        2 -> {
                            RecruitJobsTab()
                        }
                        3 -> {
                            SecondhandTab()
                        }
                        4 -> {
                            ReportMissingTab()
                        }
                    }
                }
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
                RecruitJobsTab()
            }
            AppConstants.SearchTitle.SECONDHAND_MARKET -> {
                SecondhandTab()
            }
            AppConstants.SearchTitle.REPORT_MISSING -> {
                ReportMissingTab()
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

    @OptIn(ExperimentalComposeUiApi::class)
    @Preview
    @Composable
    fun HeaderSearch(
        clickBack: (() -> Unit)? = null,
        clickSearch: ((String) -> Unit)? = null,
        init: TextFieldValue = TextFieldValue("")
    ) {
        val stateEdtInput = remember { mutableStateOf(init) }
        val keyboardController = LocalSoftwareKeyboardController.current
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
                    .padding(horizontal = 10.dp),
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
                        keyboardController?.hide()
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
                        viewModelCompany.keyword = viewModel.keyword.value
                        accessToken?.let {
                            viewModelCompany.findCompany(it, 0)
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

        LaunchedEffect(viewModel.keyword.value) {
            viewModelCompany.keyword = viewModel.keyword.value
            accessToken?.let {
                viewModelCompany.findCompany(it, 0)
            }
        }
        Column(Modifier.fillMaxSize()) {
            //TODO group sort
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //TODO: filter company
                var expandedFilter by remember { mutableStateOf(false) }
                val listFilter = remember { GetDummyData.getFilterCompany(requireContext()) }
                val itemFilterSelected = remember { mutableStateOf(listFilter[0]) }

                Image(painterResource(R.drawable.ic_filter), null)
                Box(
                    Modifier
                        .padding(start = 8.dp)
                        .size(90.dp, 26.dp)
                        .background(ColorUtils.gray_222222)
                        .noRippleClickable {
                            expandedFilter = !expandedFilter
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        itemFilterSelected.value.name!!,
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 13.sp
                    )
                    DropdownMenu(
                        expanded = expandedFilter,
                        onDismissRequest = {
                            expandedFilter = false
                        }
                    ) {
                        listFilter.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    when (selectionOption.id) {
                                        "NAGAJA_AUTHEN" -> {
                                            viewModelCompany.authentication = true
                                            viewModelCompany.filter = null
                                        }
                                        "ALL" -> {
                                            viewModelCompany.authentication = null
                                            viewModelCompany.filter = null
                                        }
                                        else -> {
                                            viewModelCompany.filter =
                                                mutableListOf(selectionOption.id!!)
                                        }
                                    }

                                    viewModelCompany.findCompany(accessToken!!, 0)
                                    itemFilterSelected.value = selectionOption
                                    expandedFilter = false
                                    showMessDEBUG(selectionOption.id ?: "null")
                                }
                            ) {
                                Text(text = selectionOption.name!!)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                //TODO: Sort company
                var expandedSort by remember { mutableStateOf(false) }
                val listSort = remember { GetDummyData.getSortCompany(requireContext()) }
                val itemSortSelected = remember { mutableStateOf(listSort[0]) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.noRippleClickable {
                        expandedSort = !expandedSort
                    }) {
                    Image(painterResource(R.drawable.ic_sort), "")
                    Text(
                        itemSortSelected.value.name!!,
                        color = ColorUtils.gray_222222,
                        fontSize = 13.sp
                    )
                    DropdownMenu(
                        expanded = expandedSort,
                        onDismissRequest = {
                            expandedSort = false
                        }
                    ) {
                        listSort.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModelCompany.sort = selectionOption.id!!
                                    viewModelCompany.findCompany(accessToken!!, 0)
                                    itemSortSelected.value = selectionOption
                                    expandedSort = false
                                    showMessDEBUG(itemSortSelected.value.id)
                                }
                            ) {
                                Text(text = selectionOption.name!!)
                            }
                        }
                    }
                }
            }

            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (viewModelCompany.stateListData.isNullOrEmpty()) {
                    Text(
                        "‘${viewModelCompany.keyword}’에 대한 검색 결과가 없습니다.",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                val listData = viewModelCompany.stateListData
                val lazyListState = rememberLazyListState()
                LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(listData) { index, obj ->
                        ItemCompany(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_COMPANY_DETAIL,
                                CompanyDetailFragment.newInstance(obj.id!!)
                            )
                        }
                        Divider(color = ColorUtils.black_000000_opacity_5)
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    accessToken?.let { viewModelCompany.findCompany(it, page) }
                }
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
                        viewModelFreeNotice.keyword = viewModel.keyword.value
                        accessToken?.let { viewModelFreeNotice.getFreeNoticeBoard(it, 0) }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        LaunchedEffect(viewModel.keyword.value) {
            viewModelFreeNotice.keyword = viewModel.keyword.value
            accessToken?.let {
                viewModelFreeNotice.getFreeNoticeBoard(it, 0)
            }
        }

        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                val listSort = remember {
                    GetDummyData.getSortLocalNews(requireContext())
                }
                var expanded1 by remember { mutableStateOf(false) }
                val itemSelected = remember { mutableStateOf(KeyValueModel()) }
                LaunchedEffect(listSort) {
                    itemSelected.value =
                        if (listSort.size > 0) listSort[0] else KeyValueModel()
                }

                Row {
                    Image(painter = painterResource(R.drawable.ic_sort), contentDescription = null)
                    Text(
                        itemSelected.value.name ?: "",
                        style = text14_222,
                        modifier = Modifier.noRippleClickable {
                            expanded1 = true
                        })
                    DropdownMenu(
                        expanded = expanded1,
                        onDismissRequest = {
                            expanded1 = false
                        }
                    ) {
                        listSort.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    itemSelected.value = selectionOption
                                    expanded1 = false
                                    viewModelFreeNotice.getFreeNoticeBoard(
                                        accessToken!!,
                                        0,
                                        selectionOption.id ?: "null"
                                    )
                                    showMessDEBUG(itemSelected.value.id)
                                }
                            ) {
                                Text(text = selectionOption.name!!)
                            }
                        }
                    }
                }
            }

            Divider(
                color = ColorUtils.black_000000,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(0.05f)
            )
            val news = viewModelFreeNotice.stateListData

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (viewModelFreeNotice.stateListData.isNullOrEmpty())
                    Text(
                        "‘${viewModelFreeNotice.keyword}’에 대한 검색 결과가 없습니다.",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
//                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    itemsIndexed(news) { index, obj ->
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
                        Divider(
                            color = ColorUtils.black_000000_opacity_5,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    viewModelFreeNotice.getFreeNoticeBoard(accessToken!!, page)
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun RecruitJobsTab() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
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
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(34.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                TabSelected(
                    modifier = Modifier.width(79.dp),
                    text = stringResource(R.string.recruitment),
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.width(79.dp),
                    text = stringResource(R.string.jobsearch),
                    isSelected = pagerState.currentPage == 1
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(1)
                    }
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Divider(color = ColorUtils.gray_222222)
                    HandleSortUI(pagerState.currentPage)
                }
            }
            SetupPagerRecruit(pagerState)
            LaunchedEffect(pagerState.currentPage) {
                if (pagerState.currentPage == 0) {
                    viewModelRecruitJobs.type = AppConstants.RECRUITMENT
                } else {
                    viewModelRecruitJobs.type = AppConstants.JOB_SEARCH
                }
            }
        }
    }

    @Composable
    private fun HandleSortUI(page: Int) {
        Row(Modifier.padding(bottom = 10.dp)) {
            val listSort = remember {
                GetDummyData.getSortRecruitmentJobs(requireContext())
            }
            var expanded1 by remember { mutableStateOf(false) }
            val itemSelected = remember { mutableStateOf(KeyValueModel()) }
            LaunchedEffect(listSort) {
                itemSelected.value =
                    if (listSort.size > 0) listSort[0] else KeyValueModel()
            }
            Image(painter = painterResource(R.drawable.ic_sort), contentDescription = null)
            Text(
                itemSelected.value.name ?: "",
                style = text14_222,
                modifier = Modifier.noRippleClickable {
                    expanded1 = true
                })

            DropdownMenu(
                expanded = expanded1,
                onDismissRequest = {
                    expanded1 = false
                }
            ) {
                listSort.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            itemSelected.value = selectionOption
                            expanded1 = false
                            viewModelRecruitJobs.sort = selectionOption.id!!
                            viewModelRecruitJobs.getRecruitmentList(
                                accessToken!!,
                                0,
                                if (page == 0) AppConstants.RECRUITMENT else AppConstants.JOB_SEARCH
                            )
                            showMessDEBUG(itemSelected.value.id)
                        }
                    ) {
                        Text(text = selectionOption.name!!)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPagerRecruit(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                RecruitmentList()
            } else {
                JobSearchList()
            }
        }
    }

    @Composable
    private fun RecruitmentList() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
//                        viewModel.type = AppConstants.RECRUITMENT
                        viewModelRecruitJobs.keyword = viewModel.keyword.value
                        viewModelRecruitJobs.getRecruitmentList(
                            accessToken!!,
                            0,
                            AppConstants.RECRUITMENT
                        )
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.keyword.value) {
            viewModelRecruitJobs.keyword = viewModel.keyword.value
            viewModelRecruitJobs.getRecruitmentList(
                accessToken!!,
                0,
                AppConstants.RECRUITMENT
            )
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            val listCompany = viewModelRecruitJobs.stateListDataRecruitment
            val lazyListState = rememberLazyListState()
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (viewModelRecruitJobs.stateListDataRecruitment.isNullOrEmpty())
                    Text(
                        "‘${viewModelRecruitJobs.keyword}’에 대한 검색 결과가 없습니다.",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(listCompany) { _, obj ->
                        ItemRecruitmentJobs(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_DETAIL,
                                RecruitJobsDetailFragment.newInstance(obj.id!!)
                            )
                        }
                        Divider(color = ColorUtils.black_000000_opacity_5)
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    viewModelRecruitJobs.getRecruitmentList(
                        accessToken!!,
                        page,
                        AppConstants.RECRUITMENT
                    )
                }
            }
        }
    }

    @Composable
    private fun JobSearchList() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModelRecruitJobs.keyword = viewModel.keyword.value
                        viewModelRecruitJobs.getRecruitmentList(
                            accessToken!!,
                            0,
                            AppConstants.JOB_SEARCH
                        )
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.keyword.value) {
            viewModelRecruitJobs.keyword = viewModel.keyword.value
            viewModelRecruitJobs.getRecruitmentList(
                accessToken!!,
                0,
                AppConstants.JOB_SEARCH
            )
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (viewModelRecruitJobs.stateListDataJobs.isNullOrEmpty())
                    Text(
                        "‘${viewModelRecruitJobs.keyword}’에 대한 검색 결과가 없습니다.",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                val listCompany = viewModelRecruitJobs.stateListDataJobs
                val lazyListState = rememberLazyListState()
                LazyColumn(state = lazyListState) {
                    itemsIndexed(listCompany) { _, obj ->
                        ItemRecruitmentJobs(obj) {

                        }
                        Divider(color = ColorUtils.black_000000_opacity_5)
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    viewModelRecruitJobs.getRecruitmentList(
                        accessToken!!,
                        page,
                        AppConstants.JOB_SEARCH
                    )
                }
            }
        }
    }

    @Composable
    private fun SecondhandTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModelSecondhand.keyword = viewModel.keyword.value
                        accessToken?.let {
                            viewModelSecondhand.getCity(it)
                            viewModelSecondhand.getListSecondHandMarket(it, 0)
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

        LaunchedEffect(viewModel.keyword.value) {
            viewModelSecondhand.keyword = viewModel.keyword.value
            viewModelSecondhand.getListSecondHandMarket(accessToken!!, 0)
        }
        Column(Modifier.fillMaxSize()) {

            //TODO: Category
            val listCategory = remember {
                GetDummyData.getSecondHandCategory().map {
                    ChooseKeyValue(it.id, it.name, false)
                }.toMutableStateList().apply {
                    add(0, ChooseKeyValue(null, "전체", true))
                }
            }
//            LazyRow(
//                modifier = Modifier
//                    .padding(vertical = 13.dp)
//                    .padding(start = 16.dp)
//            ) {
//                itemsIndexed(listCategory) { index, obj ->
//                    ItemCategory(obj, index)
//                }
//            }
            Row(
                modifier = Modifier
                    .padding(vertical = 13.dp)
                    .padding(start = 16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                listCategory.forEachIndexed { index, obj ->
                    ItemCategorySecondhand(obj) {
                        if (viewModelSecondhand.category != listCategory[index].id) {
                            listCategory.onEach {
                                it.isSelected = false
                            }
                            val newObj = listCategory[index].apply { isSelected = true }
                            listCategory.removeAt(index)
                            listCategory.add(index, newObj)
                            viewModelSecondhand.category = listCategory[index].id
                            viewModelSecondhand.getListSecondHandMarket(accessToken!!, 0)
                        }
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ColorUtils.white_FFFFFF)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                //TODO: City
                val listCity = remember {
                    viewModelSecondhand.listCity.map { cityModel ->
                        KeyValueModel(cityModel.id.toString(), cityModel.name?.get(0)?.name)
                    }.toMutableList()
                }
                BaseDropDown(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    listData = listCity,
                    onClick = { id ->
                        viewModelSecondhand.city = id
                        viewModelSecondhand.getDistrict(accessToken!!, id.toInt())
                    },
                    hintText = stringResource(R.string.choose_city),
                    hasDefaultFirstItem = false
                )
                //TODO: District
                val listDistrictInput = remember {
                    viewModelSecondhand.listDistrict.map { district ->
                        KeyValueModel(district.id.toString(), district.name?.get(0)?.name)
                    }.toMutableList()
                }
                LaunchedEffect(viewModelSecondhand.listDistrict) {
                    viewModelSecondhand.district =
                        viewModelSecondhand.listDistrict.getOrNull(0)?.id?.toString()
                }
                BaseDropDown(
                    modifier = Modifier.weight(1f),
                    listData = listDistrictInput,
                    hintText = stringResource(R.string.choose_district),
                    onClick = {
                        viewModelSecondhand.district = it
                    }
                )
            }

            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    Modifier
                        .size(120.dp, 38.dp)
                        .background(
                            color = ColorUtils.blue_2177E4,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .noRippleClickable {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_SECONDHAND_POST,
                                SecondHandRegisFragment.newInstance()
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.register_secondhand),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                val listSort = remember {
                    GetDummyData.getSortSecondHandMarket(requireContext())
                }
                var expanded1 by remember { mutableStateOf(false) }
                val itemSelected = remember { mutableStateOf(KeyValueModel()) }
                LaunchedEffect(listSort) {
                    itemSelected.value =
                        if (listSort.size > 0) listSort[0] else KeyValueModel()
                }
                Row {
                    Image(painter = painterResource(R.drawable.ic_sort), contentDescription = null)
                    Text(
                        itemSelected.value.name ?: "",
                        style = text14_222,
                        modifier = Modifier.noRippleClickable {
                            expanded1 = true
                        })
                    DropdownMenu(
                        expanded = expanded1,
                        onDismissRequest = {
                            expanded1 = false
                        }
                    ) {
                        listSort.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    itemSelected.value = selectionOption
                                    expanded1 = false
                                    viewModelSecondhand.sort = selectionOption.id!!
                                    viewModelSecondhand.getListSecondHandMarket(accessToken!!, 0)
                                    showMessDEBUG(itemSelected.value.id)
                                }
                            ) {
                                Text(text = selectionOption.name!!)
                            }
                        }
                    }
                }
            }
            Divider(color = ColorUtils.gray_BEBEBE, modifier = Modifier.padding(horizontal = 16.dp))
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (viewModelSecondhand.stateListData.isNullOrEmpty())
                    Text(
                        "‘${viewModelSecondhand.keyword}’에 대한 검색 결과가 없습니다.",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                val listSecondHand = viewModelSecondhand.stateListData
                val lazyListState = rememberLazyListState()
                LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(listSecondHand) { index, obj ->
                        ItemSecondHand(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_SECONDHAND_DETAIL,
                                SecondHandDetailFragment.newInstance(obj.id!!)
                            )
                        }
                        Divider(color = ColorUtils.gray_E1E1E1)
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    viewModelSecondhand.getListSecondHandMarket(accessToken!!, page)
                }
            }
        }
    }

    @Composable
    private fun BaseDropDown(
        modifier: Modifier = Modifier,
        isUseOtherModifier: Boolean = false,
        listData: MutableList<KeyValueModel>? = null,
        onClick: ((String) -> Unit)? = null,
        hintText: String? = null,
        hasDefaultFirstItem: Boolean = true
    ) {
        var expanded by remember { mutableStateOf(false) }
        val itemSelected = remember { mutableStateOf(KeyValueModel(hintText, hintText)) }
        if (hasDefaultFirstItem)
            LaunchedEffect(listData) {
                itemSelected.value =
                    if ((listData?.size ?: 0) > 0) listData!![0] else KeyValueModel(
                        hintText,
                        hintText
                    )
            }
        Row(
            modifier = if (isUseOtherModifier) modifier.noRippleClickable {
                expanded = true
            } else
                modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(ColorUtils.white_FFFFFF)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.blue_2177E4_opacity_10,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp)
                    .noRippleClickable {
                        expanded = true
                    },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                itemSelected.value.name ?: "",
                style = text14_62,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_dropdown),
                contentDescription = null
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                listData?.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
//                            viewModel.selectedOptionCategory.value = selectionOption
                            itemSelected.value = selectionOption
                            expanded = false
                            onClick?.invoke(selectionOption.id ?: "null")
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
    private fun ItemCategorySecondhand(obj: ChooseKeyValue, onClick: () -> Unit) {
        if (!obj.isSelected)
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .height(31.dp)
                    .border(width = 1.dp, color = ColorUtils.blue_2177E4_opacity_10)
                    .padding(horizontal = 10.dp)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(obj.name ?: "", color = ColorUtils.gray_797979)
            }
        else
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .height(31.dp)
                    .border(width = 1.dp, color = ColorUtils.blue_2177E4)
                    .padding(horizontal = 10.dp)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(obj.name ?: "", color = ColorUtils.blue_2177E4)
            }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun ReportMissingTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
//                        stateOptions.value = GetDummyData.getSortLocalNews(context)
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
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(34.dp),
//                    .border(width = 1.dp, color = ColorUtils.gray_222222),
                verticalAlignment = Alignment.Bottom
            ) {
                TabSelected(
                    modifier = Modifier.width(79.dp),
                    text = stringResource(R.string.report),
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.width(79.dp),
                    text = stringResource(R.string.missing),
                    isSelected = pagerState.currentPage == 1
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(1)
                    }
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Divider(color = ColorUtils.gray_222222)
                    HandleSortUI(pagerState.currentPage)
                }
            }
            SetupPagerReportMissing(pagerState)
            LaunchedEffect(pagerState.currentPage) {
                if (pagerState.currentPage == 0) {
                    viewModelReportMissing.type = AppConstants.REPORT
                } else {
                    viewModelReportMissing.type = AppConstants.MISSING
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPagerReportMissing(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                ReportList()
            } else {
                MissingList()
            }
        }
    }

    @Composable
    private fun ReportList() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModelReportMissing.keyword = viewModel.keyword.value
                        viewModelReportMissing.getReportMissingList(
                            accessToken!!,
                            0,
                            AppConstants.REPORT
                        )
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.keyword.value) {
            viewModelReportMissing.keyword = viewModel.keyword.value
            viewModelReportMissing.getReportMissingList(
                accessToken!!,
                0,
                AppConstants.REPORT
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (viewModelReportMissing.stateListDataReport.isNullOrEmpty())
                Text(
                    "‘${viewModelReportMissing.keyword}’에 대한 검색 결과가 없습니다.",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

            val listCompany = viewModelReportMissing.stateListDataReport
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listCompany) { _, obj ->
                    ReportMissingItem(obj) {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_REPORT_MISSING_DETAIL,
                            ReportMissingDetailFragment.newInstance(obj.id!!)
                        )
                    }
                    Divider(color = ColorUtils.black_000000_opacity_5)
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModelReportMissing.getReportMissingList(
                    accessToken!!,
                    page,
                    AppConstants.REPORT
                )
            }
        }
    }

    @Composable
    private fun MissingList() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModelReportMissing.keyword = viewModel.keyword.value
                        viewModelReportMissing.getReportMissingList(
                            accessToken!!,
                            0,
                            AppConstants.MISSING
                        )
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.keyword.value) {
            viewModelReportMissing.keyword = viewModel.keyword.value
            viewModelReportMissing.getReportMissingList(
                accessToken!!,
                0,
                AppConstants.MISSING
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (viewModelReportMissing.stateListDataMissing.isNullOrEmpty())
                Text(
                    "‘${viewModelReportMissing.keyword}’에 대한 검색 결과가 없습니다.",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

            val listCompany = viewModelReportMissing.stateListDataMissing
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listCompany) { _, obj ->
                    ReportMissingItem(obj) {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_REPORT_MISSING_DETAIL,
                            ReportMissingDetailFragment.newInstance(obj.id!!)
                        )
                    }
                    Divider(color = ColorUtils.black_000000_opacity_5)
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModelReportMissing.getReportMissingList(
                    accessToken!!,
                    page,
                    AppConstants.MISSING
                )
            }
        }
    }
}