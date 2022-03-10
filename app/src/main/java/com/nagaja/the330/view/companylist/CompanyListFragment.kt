package com.nagaja.the330.view.companylist

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.HeaderSearch
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.companydetail.CompanyDetailFragment
import com.nagaja.the330.view.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage

class CompanyListFragment : BaseFragment() {
    private lateinit var viewModel: CompanyListVM

    companion object {
        fun newInstance(cType: String? = null) = CompanyListFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.EXTRA_KEY1, cType)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[CompanyListVM::class.java]
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
                        val cType = requireArguments().getString(AppConstants.EXTRA_KEY1)
                        viewModel.cType = cType
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

        LayoutTheme330 {
            HeaderSearch(
                clickBack = {
                    viewController?.popFragment()
                },
            )
            Text(
                stringResource(R.string.company_list),
                color = ColorUtils.black_000000,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(CenterHorizontally)
            )
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
                    contentAlignment = Center
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
                                    viewModel.filter = selectionOption.id!!
                                    viewModel.findCompany(accessToken!!)
                                    itemFilterSelected.value = selectionOption
                                    expandedFilter = false
                                    showMessDEBUG(selectionOption.id!!)
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
                                    viewModel.sort = selectionOption.id!!
                                    viewModel.findCompany(accessToken!!)
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

            //TODO: Banner
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            GlideImage(
                imageModel = "",
                contentDescription = "",
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
                requestOptions = {
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenWidth * 160 / 375).dp)
            )
            Box(Modifier.padding(horizontal = 16.dp)) {
                val listData = viewModel.stateListData
                LazyColumn(state = rememberLazyListState()) {
                    itemsIndexed(listData) { index, obj ->
                        ItemCompany(obj)
                        Divider(color = ColorUtils.black_000000_opacity_5)
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemCompany(obj: CompanyModel) {
        Row(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .noRippleClickable {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_COMPANY_DETAIL,
                        CompanyDetailFragment.newInstance(obj.id!!)
                    )
                }
        ) {
            GlideImage(
                imageModel = "",
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja),
            )
            Column(Modifier.padding(start = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        obj.name?.getOrNull(0)?.name ?: "",
                        color = ColorUtils.gray_222222,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    //TODO: like button
//                    ButtonLike(obj)
                }
                Text(
                    "배달 불가능/ 예약 가능 (오늘마감)",
                    modifier = Modifier.padding(top = 7.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Text(
                    "픽업 가능 / 드랍 불가능",
                    modifier = Modifier.padding(top = 3.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomStart
                ) {

                    Text(
                        "영업시간:  09:00~20:00  (예약10:00~18:00)",
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

//    @Composable
//    private fun ButtonLike(obj: CompanyFavoriteModel) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .padding(horizontal = 6.dp)
//                .border(
//                    width = 1.dp,
//                    color = ColorUtils.gray_E1E1E1,
//                    shape = RoundedCornerShape(12.dp)
//                )
//                .padding(horizontal = 7.dp, vertical = 5.dp)
//                .noRippleClickable {
//                    viewModel.followOrNot(accessToken!!, obj.target?.id!!, obj.isFollow)
//                }
//        ) {
//            Box(Modifier.padding(end = 3.dp)) {
//                Image(
//                    painter = painterResource(R.drawable.ic_heart_content),
//                    contentDescription = null,
//                    colorFilter = ColorFilter.tint(if (obj.isFollow) ColorUtils.pink_FF4949 else ColorUtils.white_FFFFFF)
//                )
//                Image(
//                    painter = painterResource(R.drawable.ic_heart_empty),
//                    contentDescription = null
//                )
//            }
//            Text("단골 3,000", color = ColorUtils.black_000000, fontSize = 12.sp)
//        }
//    }
}