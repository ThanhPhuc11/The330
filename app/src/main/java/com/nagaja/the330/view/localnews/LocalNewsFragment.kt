package com.nagaja.the330.view.localnews

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.LocalNewsModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage

class LocalNewsFragment : BaseFragment() {
    private lateinit var viewModel: LocalNewsVM
    private var onClickSort: ((String) -> Unit)? = null

    companion object {
        fun newInstance() = LocalNewsFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[LocalNewsVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let { viewModel.getListLocalNews(it) }
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
            Header("현지뉴스") {
                viewController?.popFragment()
            }
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 13.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    val listSort = remember {
                        GetDummyData.getSortLocalNews()
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

            Divider(
                color = ColorUtils.black_000000,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .alpha(0.05f)
            )

            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(ColorUtils.black_000000_opacity_5)
            )
            {
                val news = viewModel.stateListData
                LazyColumn(state = rememberLazyListState()) {
                    itemsIndexed(news) { index, obj ->
                        ItemLocalNews(obj)
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemLocalNews(obj: LocalNewsModel) {
        Row(
            Modifier
                .padding(bottom = 1.dp)
                .fillMaxWidth()
                .background(ColorUtils.white_FFFFFF)
                .padding(vertical = 20.dp)
        ) {
            GlideImage(
//                    imageModel = "${BuildConfig.BASE_S3}${obj.images?.getOrNull(0)?.url ?: ""}",
                imageModel = "",
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
                    obj.title ?: "",
                    color = ColorUtils.black_000000,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "조회수 ${obj.viewCount ?: 0}",
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
                        "댓글 ${obj.commentCount ?: 0}",
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        HtmlCompat.fromHtml(obj.body ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            .toString(),
                        style = text14_62,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}