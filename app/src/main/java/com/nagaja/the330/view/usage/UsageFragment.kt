package com.nagaja.the330.view.usage

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.glide.GlideImage

class UsageFragment : BaseFragment() {
    private lateinit var viewModel: UsageVM

    private var options: MutableList<KeyValueModel> = mutableListOf(KeyValueModel())
//    private var stateOptions = mutableStateOf(options)

    companion object {
        fun newInstance() = UsageFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[UsageVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

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
//                        accessToken?.let {
//                            viewModel.getFavoriteCompany(
//                                it,
//                                0,
//                                stateOptions.value[0].id!!
//                            )
//                        }
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

        LayoutTheme330 {
            Header(title = stringResource(R.string.usage_list)) {
                viewController?.popFragment()
            }
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(stringResource(R.string.a_total_of_use, 30), style = text14_222)
                    HandleSortUI(modifier = Modifier.weight(1f))
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(ColorUtils.gray_E1E1E1)
                ) {
                    val listCompany = viewModel.listCompany.apply {
                        add(CompanyFavoriteModel())
                        add(CompanyFavoriteModel())
                        add(CompanyFavoriteModel())
                    }
                    LazyColumn(state = rememberLazyListState()) {
                        itemsIndexed(listCompany) { _, obj ->
                            CompanyItem(obj)
                        }
                    }
                }

            }
        }
    }

    @Composable
    private fun HandleSortUI(modifier: Modifier = Modifier) {
        val options = GetDummyData.getSortFavoriteCompany(LocalContext.current)
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        Box(
            modifier = modifier,
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
    private fun CompanyItem(obj: CompanyFavoriteModel) {
        Row(
            Modifier
                .padding(bottom = 1.dp)
                .background(ColorUtils.white_FFFFFF)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = "",
                Modifier.size(96.dp),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja)
            )
            Column(Modifier.padding(start = 9.dp)) {
                Text(
                    obj.target?.name?.get(0)?.name ?: "",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "단골 등록일: 2021년 10월 26일",
                    modifier = Modifier.padding(top = 3.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Text(
                    "이용 건수: ${obj.usageCount ?: 0}회",
                    modifier = Modifier.padding(top = 3.dp),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp
                )
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        Modifier
                            .width(76.dp)
                            .height(32.dp)
                            .background(ColorUtils.gray_222222, shape = RoundedCornerShape(99.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("재예약", color = ColorUtils.white_FFFFFF, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}