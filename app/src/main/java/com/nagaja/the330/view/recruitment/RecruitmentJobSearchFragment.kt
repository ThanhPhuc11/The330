package com.nagaja.the330.view.recruitment

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.*
import com.nagaja.the330.view.*
import com.nagaja.the330.view.recruitmentdetail.RecruitJobsDetailFragment
import com.nagaja.the330.view.recruitmentregis.RecruitJobRegisFragment
import com.nagaja.the330.view.searchmain.ItemRecruitmentJobs
import com.nagaja.the330.view.searchmain.SearchMainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecruitmentJobSearchFragment : BaseFragment() {
    private lateinit var viewModel: RecruitmentJobSearchVM
    private var onClickSort: ((String) -> Unit)? = null

    private var options: MutableList<KeyValueModel> = mutableListOf(KeyValueModel())
//    private var stateOptions = mutableStateOf(options)

    companion object {
        fun newInstance() = RecruitmentJobSearchFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[RecruitmentJobSearchVM::class.java]
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
        val context = LocalContext.current
        val owner = LocalLifecycleOwner.current
        val stateOptions = remember { mutableStateOf(options) }
        val stateOpenDialog = remember {
            mutableStateOf(false)
        }
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        stateOptions.value = GetDummyData.getSortLocalNews(context)
                        viewModel.existJobSearch.observe(viewLifecycleOwner) {
                            if (!it) {
                                viewController?.pushFragment(
                                    ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_REGIS,
                                    RecruitJobRegisFragment.newInstance(viewModel.type)
                                )
                            } else {
                                stateOpenDialog.value = true
                            }
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

        if (stateOpenDialog.value) {
            Dialog2Button(
                state = stateOpenDialog,
                title = "구인/구직 글은 1개만\n등록할 수 있습니다.",
                content = "기존의 구직 글을 삭제하고\n" +
                        "새로 등록하시겠습니까?\n" +
                        "* 확인을 클릭하시면 기존 작성된\n" +
                        "구직 글이 삭제됩니다.",
                leftText = getString(R.string.cancel),
                rightText = getString(R.string.confirm),
                onClick = {
                    if (it) {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_REGIS,
                            RecruitJobRegisFragment.newInstance(viewModel.type)
                        )
                    }
                }
            )
        }
        LayoutTheme330 {
            HeaderSearch(
                clickBack = {
                    viewController?.popFragment()
                },
                textOption = stringResource(R.string.post_register),
                clickOption = {
                    if (userDetailBase?.userType == AppConstants.COMPANY) {
                        if (viewModel.type == AppConstants.RECRUITMENT) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_RECRUITMENT_JOBSEARCH_REGIS,
                                RecruitJobRegisFragment.newInstance(viewModel.type)
                            )
                        }
                    } else {
                        if (viewModel.type == AppConstants.JOB_SEARCH) {
                            viewModel.checkExistJobSearch(accessToken!!)
                        }
                    }
                },
                clickSearch = {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_SEARCH_MAIN,
                        SearchMainFragment.newInstance(it, 2)
                    )
                }
            )
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
            SetupPager(pagerState)
            LaunchedEffect(pagerState.currentPage) {
                if (pagerState.currentPage == 0) {
                    viewModel.type = AppConstants.RECRUITMENT
                } else {
                    viewModel.type = AppConstants.JOB_SEARCH
                }
            }
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
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
                        viewModel.getRecruitmentList(accessToken!!, 0, AppConstants.RECRUITMENT)
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            val listCompany = viewModel.stateListDataRecruitment
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
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
                viewModel.getRecruitmentList(accessToken!!, page, AppConstants.RECRUITMENT)
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
//                        viewModel.type = AppConstants.JOB_SEARCH
                        viewModel.getRecruitmentList(accessToken!!, 0, AppConstants.JOB_SEARCH)
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {
            val listCompany = viewModel.stateListDataJobs
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
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
                viewModel.getRecruitmentList(accessToken!!, page, AppConstants.JOB_SEARCH)
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
            onClickSort = { id ->
                viewModel.sort = id
                viewModel.getRecruitmentList(
                    accessToken!!,
                    0,
                    if (page == 0) AppConstants.RECRUITMENT else AppConstants.JOB_SEARCH
                )
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
}