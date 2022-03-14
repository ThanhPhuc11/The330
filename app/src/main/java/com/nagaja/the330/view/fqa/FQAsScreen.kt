package com.nagaja.the330.view.fqa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.FQAModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.R
import com.nagaja.the330.ui.theme.textBold16
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class FQAsScreen: BaseFragment() {
    private lateinit var viewModel: FQAViewModel

    companion object {
        fun newInstance() = FQAsScreen()
    }


    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FQAViewModel::class.java]
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
                        accessToken?.let { viewModel.getFQAs(it) }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        LayoutTheme330{
            Header(stringResource(R.string.title_fqa)) {
                viewController?.popFragment()
            }
            Box(
                Modifier
                    .background(ColorUtils.white_FFFFFF)
                    .padding(top = 35.dp)
            ){
                val fqas = viewModel.fqaStateList
                Column{
                    LazyColumn(state = rememberLazyListState()) {
                        itemsIndexed(fqas) {_, fqa -> ItemFQA(item = fqa)}
                    }

                    Divider(
                        Modifier.height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(ColorUtils.gray_00000D)
                    )
                }
            }
        }
    }

    @Composable
    private fun ItemFQA(item: FQAModel) {
        Row(
            Modifier
                .padding(start = 16.dp, end = 16.dp)
                .clickable {
                    item.id?.let {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_FQA_DETAIL,
                            FQAsDetailScreen.newInstance(it)
                        )
                    }
                }
        ) {
            Column{
                Divider(modifier = Modifier
                    .height(1.dp)
                    .background(ColorUtils.gray_00000D)
                )
                Text(
                    text = item.question ?: "",
                    Modifier.padding(top = 16.dp),
                    style = textBold16
                )
                Row(
                    Modifier.padding(top = 5.dp, bottom = 16.dp)
                ){
                    Text(
                        text = item.createdOn ?: "",
                        style = textRegular12
                    )
                    Text(
                        text = ".",
                        style = textRegular12
                    )
                    Text(
                        text = "조회수 ${item.viewCount}",
                        style = textRegular12
                    )
                }
            }
        }
    }
}