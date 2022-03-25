package com.nagaja.the330.view.secondhandmarket

import androidx.compose.foundation.*
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.searchmain.ItemSecondHand
import com.nagaja.the330.view.secondhanddetail.SecondHandDetailFragment
import com.nagaja.the330.view.secondhandregis.SecondHandRegisFragment

class SecondHandMarketFragment : BaseFragment() {
    private lateinit var viewModel: SecondHandMarketVM
    private var onClickChoose: ((Int) -> Unit)? = null
    private var onClickSort: ((String) -> Unit)? = null

    companion object {
        fun newInstance() = SecondHandMarketFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[SecondHandMarketVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
//        val listCategory = GetDummyData.getSecondHandCategory()

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
//                        viewModel.category = listCategory[0].id!!
                        accessToken?.let {
                            viewModel.getCity(it)
                            viewModel.getListSecondHandMarket(it, 0)
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
        LayoutTheme330 {
            HeaderSearch(
                clickBack = {
                    viewController?.popFragment()
                },
                clickSearch = {
                    showMessDEBUG(it)
                }
            )

            //TODO: Category
            val listCategory = remember {
                GetDummyData.getSecondHandCategory().map {
                    ChooseKeyValue(it.id, it.name, false)
                }.toMutableStateList().apply {
                    add(0, ChooseKeyValue(null, "전체", true))
                }
            }
            onClickChoose = { index ->
                if (viewModel.category != listCategory[index].id) {
                    listCategory.onEach {
                        it.isSelected = false
                    }
                    val newObj = listCategory[index].apply { isSelected = true }
                    listCategory.removeAt(index)
                    listCategory.add(index, newObj)
                    viewModel.category = listCategory[index].id
                    viewModel.getListSecondHandMarket(accessToken!!, 0)
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
                    ItemCategorySecondhand(obj, index)
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
                    viewModel.listCity.map { cityModel ->
                        KeyValueModel(cityModel.id.toString(), cityModel.name?.get(0)?.name)
                    }.toMutableList()
                }
                BaseDropDown(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    listData = listCity,
                    onClick = { id ->
                        viewModel.city = id
                        viewModel.getDistrict(accessToken!!, id.toInt())
                    },
                    hintText = stringResource(R.string.choose_city),
                    hasDefaultFirstItem = false
                )
                //TODO: District
                val listDistrictInput = remember {
                    viewModel.listDistrict.map { district ->
                        KeyValueModel(district.id.toString(), district.name?.get(0)?.name)
                    }.toMutableList()
                }
                LaunchedEffect(viewModel.listDistrict) {
                    viewModel.district = viewModel.listDistrict.getOrNull(0)?.id?.toString()
                }
                BaseDropDown(
                    modifier = Modifier.weight(1f),
                    listData = listDistrictInput,
                    hintText = stringResource(R.string.choose_district),
                    onClick = {
                        viewModel.district = it
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
                onClickSort = { id ->
                    viewModel.sort = id
                    viewModel.getListSecondHandMarket(accessToken!!, 0)
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
            Divider(color = ColorUtils.gray_BEBEBE, modifier = Modifier.padding(horizontal = 16.dp))
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(ColorUtils.gray_E7E7E7)
            ) {
                val listSecondHand = viewModel.stateListData
                val lazyListState = rememberLazyListState()
                LazyColumn(state = lazyListState) {
                    itemsIndexed(listSecondHand) { index, obj ->
                        ItemSecondHand(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_SECONDHAND_DETAIL,
                                SecondHandDetailFragment.newInstance(obj.id!!)
                            )
                        }
                    }
                }

                LoadmoreHandler(lazyListState) { page ->
                    viewModel.getListSecondHandMarket(accessToken!!, page)
                }
            }
        }
    }

    @Composable
    private fun ItemCategorySecondhand(obj: ChooseKeyValue, index: Int) {
        if (!obj.isSelected)
            Box(
                Modifier
                    .padding(end = 6.dp)
                    .height(31.dp)
                    .border(width = 1.dp, color = ColorUtils.blue_2177E4_opacity_10)
                    .padding(horizontal = 10.dp)
                    .noRippleClickable {
                        onClickChoose?.invoke(index)
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
                        onClickChoose?.invoke(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(obj.name ?: "", color = ColorUtils.blue_2177E4)
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
}