package com.nagaja.the330.view.secondhandmypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
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
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.SecondHandModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondHandMypageFragment : BaseFragment() {
    private lateinit var viewModel: SecondHandMypageVM

    private var options: MutableList<KeyValueModel> = mutableListOf(KeyValueModel())
//    private var stateOptions = mutableStateOf(options)

    companion object {
        fun newInstance() = SecondHandMypageFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SecondHandMypageVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

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
                        stateOptions.value = GetDummyData.getSortFavoriteCompany(context)
                        accessToken?.let {
                            viewModel.getMySecondHand(it, 0)
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
            Header(title = stringResource(R.string.second_hand)) {
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
                SecondHandPurchaseList()
            } else {
                ConsultationList()
            }
        }
    }

    @Composable
    private fun SecondHandPurchaseList() {
        Column(Modifier.fillMaxHeight()) {
            HandleSortUI()

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorUtils.gray_E1E1E1)
            )
            val listSecondhand = viewModel.stateListSecondhand
            val lazyListState = rememberLazyListState()
            LazyColumn(state = lazyListState) {
                itemsIndexed(listSecondhand) { _, obj ->
                    SecondhandItem(obj)
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getMySecondHand(accessToken!!, page)
            }
        }
    }

    @Composable
    private fun ConsultationList() {
        Column(Modifier.fillMaxSize()) {
            val listCompany = mutableListOf<CompanyFavoriteModel>().apply {
                add(CompanyFavoriteModel())
                add(CompanyFavoriteModel())
                add(CompanyFavoriteModel())
            }
            LazyColumn(state = rememberLazyListState()) {
                itemsIndexed(listCompany) { _, obj ->
                    ConsultationItem()
                }
            }
        }
    }

    @Composable
    private fun HandleSortUI() {
        val options = GetDummyData.getSortMySecondHand(LocalContext.current)
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    }
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 10.dp)
                    .width(100.dp)
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedOptionText.name ?: "",
                    style = text14_222,
                    color = ColorUtils.black_000000,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
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
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                if (selectionOption.id == "LASTEST" || selectionOption.id == "VIEW_COUNT") {
                                    viewModel.sort = selectionOption.id
                                    viewModel.transactionStatus = null
                                } else {
                                    viewModel.sort = null
                                    viewModel.transactionStatus = selectionOption.id
                                }
                                viewModel.getMySecondHand(accessToken!!, 0)
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SecondhandItem(obj: SecondHandModel) {
        Column(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                        "판매완료",
                        color = if (obj.transactionStatus == "TRANSACTION_COMPLETE") ColorUtils.blue_2177E4 else ColorUtils.white_FFFFFF,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "[${obj.type}] ${obj.title}",
                        modifier = Modifier.padding(top = 1.dp),
                        color = ColorUtils.black_000000,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700)
                    )
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            price(obj),
                            color = ColorUtils.black_000000,
                            fontSize = 22.sp,
                            fontWeight = FontWeight(700)
                        )
                    }
                }
            }
            Text(
                "${obj.body}",
                style = text14_62,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 16.dp)
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

    @Preview
    @Composable
    private fun ConsultationItem() {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
        ) {
            Text(
                "게시글제목 게시글제목 게시글제목",
                color = ColorUtils.gray_222222,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                "mintkim",
                modifier = Modifier.padding(top = 5.dp),
                color = ColorUtils.gray_9F9F9F,
                fontSize = 12.sp
            )
            Text(
                "금액 조정 가능할까요?! 금액 조정 가능할까요?! 금액 조정 가능할까요?!금액 조정 가능할까요?!금액 조정 가능할까요?!",
                modifier = Modifier.padding(top = 5.dp),
                style = text14_62,
                textAlign = TextAlign.Start
            )
            Text(
                "{YYYY/MM/DD(최초 대화일시)}\n" +
                        "{YYYY/MM/DD(최종 대화일시)}",
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

    private fun price(secondhand: SecondHandModel): String {
        return if ((secondhand.dollar ?: 0.0) > 0) {
            GetDummyData.getMoneyType()[1].name!!.plus(" ").plus(secondhand.dollar)
        } else {
            GetDummyData.getMoneyType()[0].name!!.plus(" ").plus(secondhand.peso)
        }
    }
}