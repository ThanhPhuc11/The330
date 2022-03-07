package com.nagaja.the330.view.reportmissing

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.ReportMissingModel
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportMissingFragment : BaseFragment() {
    private lateinit var viewModel: ReportMissingVM
    private var onClickSort: ((String) -> Unit)? = null

    private var options: MutableList<KeyValueModel> = mutableListOf(KeyValueModel())
//    private var stateOptions = mutableStateOf(options)

    companion object {
        fun newInstance() = ReportMissingFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ReportMissingVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun UIData() {
        val context = LocalContext.current
        val owner = LocalLifecycleOwner.current
        val stateOptions = remember { mutableStateOf(options) }
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        stateOptions.value = GetDummyData.getSortLocalNews(context)
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
            HeaderSearch(
                clickBack = {
                    viewController?.popFragment()
                },
                textOption = stringResource(R.string.post_register),
                clickOption = {

                }
            )
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
                    HandleSortUI()
                }
            }
            SetupPager(pagerState)
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
                    .border(width = 1.dp, color = ColorUtils.gray_222222)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = ColorUtils.gray_222222, fontSize = 14.sp)
            }
        else
            Box(
                modifier
                    .background(ColorUtils.gray_222222)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = ColorUtils.gray_222222)
                    .noRippleClickable {
                        onClick.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text,
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 14.sp
                )
            }

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SetupPager(pagerState: PagerState) {
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
                        viewModel.getListLocalNews(accessToken!!, "LASTEST", "REPORT")
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
            val listCompany = viewModel.stateListDataReport
            LazyColumn(state = rememberLazyListState()) {
                itemsIndexed(listCompany) { _, obj ->
                    SecondhandItem(obj)
                    Divider(color = ColorUtils.black_000000_opacity_5)
                }
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
                        viewModel.getListLocalNews(accessToken!!, "LASTEST", "MISSING")
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
            val listCompany = viewModel.stateListDataMissing
            LazyColumn(state = rememberLazyListState()) {
                itemsIndexed(listCompany) { _, obj ->
                    SecondhandItem(obj)
                    Divider(color = ColorUtils.black_000000_opacity_5)
                }
            }
        }
    }

    @Composable
    private fun HandleSortUI() {
        Row(Modifier.padding(bottom = 10.dp)) {
            val listSort = remember {
                GetDummyData.getSortLocalNews(requireContext())
            }
            var expanded1 by remember { mutableStateOf(false) }
            val itemSelected = remember { mutableStateOf(KeyValueModel()) }
            LaunchedEffect(listSort) {
                itemSelected.value =
                    if (listSort.size > 0) listSort[0] else KeyValueModel()
            }
            onClickSort = { id ->
                viewModel.getListLocalNews(accessToken!!, id)
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

    @Composable
    private fun SecondhandItem(obj: ReportMissingModel) {
        Column(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(top = 16.dp)
        ) {
            Row {
                GlideImage(
                    imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                    Modifier
                        .size(96.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    placeHolder = painterResource(R.drawable.ic_default_nagaja),
                    error = painterResource(R.drawable.ic_default_nagaja),
                )
                Column(
                    Modifier
                        .padding(start = 12.dp)
                        .height(96.dp)
                ) {
                    Text(
                        obj.title ?: "",
                        modifier = Modifier.padding(top = 1.dp),
                        color = ColorUtils.black_000000,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                            color = ColorUtils.gray_9F9F9F,
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
                            stringResource(R.string.comment).plus(" ${obj.commentCount ?: 0}"),
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                    }
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            obj.body ?: "",
                            style = text14_62,
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_15,
                        obj.createdOn ?: ""
                    ),
                    color = ColorUtils.gray_9F9F9F,
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
                    obj.writer?.name ?: "",
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                )
            }
        }
    }
}