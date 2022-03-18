package com.nagaja.the330.view.regularcustomer

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegularCustomerFragment: BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = RegularCustomerFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun UIData() {

        val pagerState = rememberPagerState(pageCount = 2)

        LayoutTheme330 {
            Header(stringResource(R.string.option_regular_list)) {
                viewController?.popFragment()
            }
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
                    text = "기업 단골 목록",
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = "내 단골 목록",
                    isSelected = pagerState.currentPage == 1
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(1)
                    }
                }
            }
            SetupPager(requireContext(), pagerState = pagerState)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(context: Context, pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                CompanyRegularCustomerTab(context = context)
            } else {
                MyRegularCustomerTab(context = context)
            }
        }
    }

    @Composable
    private fun CompanyRegularCustomerTab(context: Context) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(context = context, GetDummyData.getSortFavoriteCompany(context = context))
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            // TODO replace dummy data
            val data = GetDummyData.getCompanyUsageList()
            LazyColumn(Modifier.padding(top = 16.dp),
                state = rememberLazyListState()
            ) {
                itemsIndexed(data) {_, item ->
                    ItemRegular(item)
                }
            }
        }
    }

    @Composable
    private fun MyRegularCustomerTab(context: Context) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(context = context, GetDummyData.getSortFavoriteCompany(context = context))
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            // TODO replace dummy data
            val data = GetDummyData.getCompanyUsageList()
            LazyColumn(Modifier.padding(top = 16.dp),
                state = rememberLazyListState()
            ) {
                itemsIndexed(data) {_, item ->
                    ItemRegular(item)
                }
            }
        }
    }
}