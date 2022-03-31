package com.nagaja.the330.view.freenoticeboard

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.FreeNoticeModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.HeaderSearch
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.freenoticedetail.FreeNoticeDetailFragment
import com.nagaja.the330.view.freenoticeregis.FreeNoticeRegisFragment
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.searchmain.SearchMainFragment
import com.nagaja.the330.view.text14_222

class FreeNoticeFragment : BaseFragment() {
    private lateinit var viewModel: FreeNoticeVM
    private var onClickSort: ((String) -> Unit)? = null
    private var onClickDetail: ((FreeNoticeModel) -> Unit)? = null

    companion object {
        fun newInstance() = FreeNoticeFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FreeNoticeVM::class.java]
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
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let { viewModel.getFreeNoticeBoard(it, 0) }
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
                clickBack = {
                    viewController?.popFragment()
                },
                clickSearch = {
                    showMessDEBUG(it)
                    viewController?.pushFragment(
                        ScreenId.SCREEN_SEARCH_MAIN,
                        SearchMainFragment.newInstance(it, 1)
                    )
                },
                textOption = stringResource(R.string.post_register),
                clickOption = {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_FREE_NOTICE_REGIS,
                        FreeNoticeRegisFragment.newInstance()
                    )
                }
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.free_board),
                    color = ColorUtils.black_000000,
                    fontSize = 17.sp
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 14.dp)
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
                onClickSort = { id ->
                    viewModel.getFreeNoticeBoard(accessToken!!, 0, id)
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

            Divider(
                color = ColorUtils.black_000000,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(0.05f)
            )
            val news = viewModel.stateListData
            onClickDetail = {
                if (it.notice != true) {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_FREE_NOTICE_DETAIL,
                        FreeNoticeDetailFragment.newInstance(it.id!!)
                    )
                }
            }
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
//                    verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                itemsIndexed(news) { index, obj ->
                    if (obj.status == "DEACTIVATED") {
                        ItemDisable()
                    } else {
                        ItemFreeNotice(obj)
                    }
                    Divider(
                        color = ColorUtils.black_000000_opacity_5,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getFreeNoticeBoard(accessToken!!, page)
            }
        }
    }

    @Preview
    @Composable
    private fun ItemDisable() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(97.dp)
                .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text(
                stringResource(R.string.This_post_has_been_made_private_by_the_administrator),
                style = text14_222
            )
        }
    }

    @Composable
    private fun ItemFreeNotice(obj: FreeNoticeModel) {
        Row(
            Modifier
                .background(
                    when {
                        obj.notice == true -> {
                            ColorUtils.blue_2177E4_opacity_5
                        }
                        obj.top == true -> {
                            ColorUtils.yellow_FFB800_opacity_5
                        }
                        else -> {
                            ColorUtils.white_FFFFFF
                        }
                    }
                )
                .padding(16.dp)
                .fillMaxWidth()
                .noRippleClickable {
                    onClickDetail?.invoke(obj)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "[${obj.id}] ${
                        AppDateUtils.changeDateFormat(
                            AppDateUtils.FORMAT_16,
                            AppDateUtils.FORMAT_15,
                            obj.createdOn ?: ""
                        )
                    }", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp
                )
                Text(
                    obj.title ?: "",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(
                        obj.user?.name ?: "",
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
                        stringResource(R.string.views).plus(" ${obj.viewCount ?: 0}"),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                }
            }
            Box(
                Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(200.dp))
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(200.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("${obj.commentCount ?: 0}", color = ColorUtils.gray_9F9F9F, fontSize = 14.sp)
            }
        }
    }
}