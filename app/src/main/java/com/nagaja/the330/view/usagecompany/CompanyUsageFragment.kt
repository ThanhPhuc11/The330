package com.nagaja.the330.view.usagecompany

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
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
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CompanyUsageModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.nagaja.the330.view.usage.UsageVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompanyUsageFragment : BaseFragment() {
    private lateinit var viewModel: UsageVM

    companion object {
        @JvmStatic
        fun newInstance() =
            CompanyUsageFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[UsageVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
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
            Header(stringResource(R.string.option_usage_list)) {
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
                    text = "기업 이용 목록",
                    isSelected = pagerState.currentPage == 0
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(0)
                    }
                }
                TabSelected(
                    modifier = Modifier.weight(1f),
                    text = "내 이용 목록",
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
    private fun SetupPager(context: Context, pagerState: PagerState) {
        HorizontalPager(state = pagerState) { page ->
            if (page == 0) {
                CorporateUseTab(context)
            } else {
                MyUsageTab(context = context)
            }
        }
    }

    @Composable
    private fun CorporateUseTab(context: Context) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(context = context)
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            val data = GetDummyData.getCompanyUsageList()
            LazyColumn(Modifier.padding(top = 16.dp),
                state = rememberLazyListState()) {
                itemsIndexed(data) {_, item ->
                    ItemUsage(item)
                }
            }
        }
    }

    @Composable
    private fun MyUsageTab(context: Context) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                HandleSortUI(context = context)
            }
            Divider(color = ColorUtils.gray_E1E1E1)
            val data = GetDummyData.getCompanyUsageList()
            LazyColumn(Modifier.padding(top = 16.dp),
                state = rememberLazyListState()) {
                itemsIndexed(data) {_, item ->
                    ItemUsage(item)
                }
            }
        }
    }

    @Composable
    fun HandleSortUI(context: Context) {
        val filters = remember {
            // Dummy data
            GetDummyData.getSortReservationRoleCompany(context = context)
        }
        var expanded by remember { mutableStateOf(false) }
        val itemSelected = remember { mutableStateOf(filters[0])}
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
            Text(
                itemSelected.value.name ?: "",
                modifier = Modifier.weight(1f),
                style = text14_222
            )
            Image(
                painterResource(R.drawable.ic_arrow_down), null,
                Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .width(10.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                filters.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            itemSelected.value = option
                            expanded = false
                            if (BuildConfig.DEBUG)
                                Toast.makeText(context, "${itemSelected.value.id}", Toast.LENGTH_LONG).show()
                        }
                    ) {
                        option.name?.let { Text(text = it) }
                    }

                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ItemUsage(item: CompanyUsageModel = CompanyUsageModel().apply {
        id = 0
        name = "name"
        date = "2022/03/18 00:00"
        numberOfUsers = 100
    }){
        Row(Modifier.padding(16.dp)
            .height(IntrinsicSize.Max)
        ) {
            Column(
                Modifier.weight(5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text("${item.name}(${item.id})",
                    modifier = Modifier.fillMaxWidth()
                        .padding(end = 2.dp),
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start,
                )
                Text("이용건수: ${item.numberOfUsers}회",
                    modifier = Modifier.padding(top = 12.dp)
                        .fillMaxWidth()
                        .padding(end = 2.dp),
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(6f),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(modifier = Modifier.fillMaxWidth(),
                    text = "이용일시:${item.date}",
                    color = ColorUtils.black_000000,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
        Divider(
            Modifier
                .height(1.dp)
                .background(ColorUtils.gray_E1E1E1)
        )
    }
}


