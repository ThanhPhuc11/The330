package com.nagaja.the330.view.faq

import android.annotation.SuppressLint
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.FAQModel
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

class FAQsFragment: BaseFragment() {
    private lateinit var viewModel: FAQViewModel

    companion object {
        fun newInstance() = FAQsFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FAQViewModel::class.java]
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
        LayoutTheme330 {
            Header(stringResource(R.string.title_faq)) {
                viewController?.popFragment()
            }
            val faqs = viewModel.fqaStateList
            Body(faqs = faqs)
        }
    }

    @Composable
    private fun Body(faqs: SnapshotStateList<FAQModel>) {
        Box(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .padding(top = 35.dp)
        ){
            Column{
                LazyColumn(state = rememberLazyListState()) {
                    itemsIndexed(faqs) {_, faq -> ItemFQA(item = faq)}
                }

                Divider(
                    Modifier
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(ColorUtils.gray_00000D)
                )
            }
        }
    }

    @Composable
    private fun ItemFQA(item: FAQModel) {
        Row(
            Modifier
                .padding(start = 16.dp, end = 16.dp)
                .clickable {
                    item.id?.let {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_FQA_DETAIL,
                            FAQsDetailFragment.newInstance(it)
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
                        text = "·",
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

    @SuppressLint("UnrememberedMutableState")
    @Preview
    @Composable
    fun faqsPreview() {
        val faqs = mutableStateListOf<FAQModel>()
        val list = mutableListOf<FAQModel>()
        for ( i in 0..100) {
            list.add(FAQModel().apply {
                id = i
                question = "군인은 현역을 $i"
                answer = "<p>군인은 현역을 면한 후가 아니면 국무총리로 임명될 수 없다. 대통령은 즉시 이를 공포하여야 한다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 국회의원의 수는 법률로 정하되.</p>"
                status = "ACTIVATED"
                priority = 7
                viewCount = 100
                createdOn = "2022.03.16"
            })
        }
        faqs.addAll(list)
        LayoutTheme330 {
            Header(stringResource(R.string.title_faq)) {
                viewController?.popFragment()
            }
            Body(faqs = faqs)
        }
    }
}