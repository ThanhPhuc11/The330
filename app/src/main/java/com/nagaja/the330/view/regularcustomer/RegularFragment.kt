package com.nagaja.the330.view.regularcustomer

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegularFragment : BaseFragment() {
    private lateinit var viewModel: RegularVM

    companion object {
        @JvmStatic
        fun newInstance() = RegularFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[RegularVM::class.java]
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
            SetupPager(pagerState = pagerState)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                CompanyRegularCustomerTab()
            } else {
                MyRegularCustomerTab()
            }
        }
    }

    @Composable
    private fun CompanyRegularCustomerTab() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getLikeMe(accessToken!!, 0)
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        val context = LocalContext.current
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
                        text = "총 ${viewModel.totalLikeMe}건 이용",
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
                        context = context,
                        GetDummyData.getSortFavoriteCompany(context = context)
                    ) {}
                }
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            LazyColumn(
                state = rememberLazyListState()
            ) {
                itemsIndexed(viewModel.listLikeMe) { _, item ->
                    ItemRegular(item)
                }
            }
        }
    }

    @Composable
    private fun MyRegularCustomerTab() {
        val context = LocalContext.current
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getMyLike(accessToken!!, 0)
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
                        text = "총 ${viewModel.totalMyLike}건 이용",
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
                        context = context,
                        GetDummyData.getSortFavoriteCompany(context = context)
                    ) {}
                }
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            LazyColumn(
                state = rememberLazyListState()
            ) {
                itemsIndexed(viewModel.listMyLike) { _, item ->
                    ItemRegular(item)
                }
            }
        }
    }

    @Composable
    fun ItemRegular(obj: CompanyFavoriteModel) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "${obj.target?.name?.getOrNull(0)?.name}",
                modifier = Modifier
                    .fillMaxWidth(),
                color = ColorUtils.black_000000,
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    "사용인원: ${obj.target?.usageCount}인",
                    modifier = Modifier.weight(1f),
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start,
                )

                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "단골 등록일:${
                        AppDateUtils.changeDateFormat(
                            AppDateUtils.FORMAT_16,
                            AppDateUtils.FORMAT_5,
                            obj.createdOn ?: ""
                        )
                    }",
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}