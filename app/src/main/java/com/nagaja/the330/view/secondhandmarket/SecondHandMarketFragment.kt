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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.SecondHandModel
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage

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
                            viewModel.getListSecondHandMarket(it)
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
                }.toMutableList()
            }
            onClickChoose = { index ->
//                val temp = listCategory[index].apply {
//                    isSelected = !isSelected
//                }
//                val listTemp = mutableListOf<ChooseKeyValue>().apply {
//                    addAll(listCategory)
//                }
//                listTemp.onEach { obj ->
//                    obj.isSelected = false
//                }
//                listTemp.removeAt(index)
//                listTemp.add(index, temp)
//                listCategory.clear()
//                listCategory.addAll(listTemp)
                listCategory[index].isSelected = true
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
                    ItemCategory(obj, index)
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ColorUtils.white_FFFFFF)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                //TODO: City
                BaseDropDown(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    listData = viewModel.listCity.map { cityModel ->
                        KeyValueModel(cityModel.id.toString(), cityModel.name?.get(0)?.name)
                    }.toMutableList(),
                    onClick = { id ->
                        viewModel.city = id
                        viewModel.getDistrict(accessToken!!, id.toInt())
                    },
                    hintText = "시/도",
                    hasDefaultFirstItem = false
                )
                //TODO: District
                LaunchedEffect(viewModel.listDistrict) {
                    viewModel.district = viewModel.listDistrict.getOrNull(0)?.id?.toString()
                }
                BaseDropDown(
                    modifier = Modifier.weight(1f),
                    listData = viewModel.listDistrict.map { district ->
                        KeyValueModel(district.id.toString(), district.name?.get(0)?.name)
                    }.toMutableList(),
                    hintText = "구/군",
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
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("중고판매등록", color = ColorUtils.white_FFFFFF, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))

                val listSort = GetDummyData.getSortSecondHandMarket()
                var expanded1 by remember { mutableStateOf(false) }
                val itemSelected = remember { mutableStateOf(KeyValueModel()) }
                LaunchedEffect(listSort) {
                    itemSelected.value =
                        if (listSort.size > 0) listSort[0] else KeyValueModel()
                }
                onClickSort = { id ->
                    viewModel.getListSecondHandMarket(accessToken!!, id)
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
                LazyColumn(state = rememberLazyListState()) {
                    itemsIndexed(listSecondHand) { index, obj ->
                        ItemSecondHand(obj)
                    }
                }
            }
        }
    }

    class ChooseKeyValue(
        var id: String? = null,
        var name: String? = null,
        var isSelected: Boolean = false
    )

    @Composable
    private fun ItemCategory(obj: ChooseKeyValue, index: Int) {
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
                painter = painterResource(com.nagaja.the330.R.drawable.ic_arrow_dropdown),
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
    private fun ItemSecondHand(obj: SecondHandModel) {
        Column(
            Modifier
                .padding(bottom = 1.dp)
                .fillMaxWidth()
                .background(ColorUtils.white_FFFFFF)
                .padding(vertical = 20.dp)
        ) {
            Row {
                GlideImage(
                    imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                    Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    placeHolder = painterResource(R.drawable.ic_default_nagaja),
                    error = painterResource(R.drawable.ic_default_nagaja)
                )
                Column(
                    Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                        .height(96.dp)
                ) {
                    Text(
                        "[${obj.type}] ${obj.title}",
                        color = ColorUtils.black_000000,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Row {
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
                            "조회수 ${obj.viewCount ?: 0}",
                            color = ColorUtils.gray_9F9F9F,
                            fontSize = 12.sp,
                        )
                    }
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            price(obj),
                            color = ColorUtils.black_000000,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Text(
                obj.body ?: "",
                style = text14_62,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Start
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