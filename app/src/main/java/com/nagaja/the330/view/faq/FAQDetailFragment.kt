package com.nagaja.the330.view.faq

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.FAQModel
import com.nagaja.the330.ui.theme.textBold18
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.AppConstants.EXTRA_KEY1
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class FAQsDetailFragment: BaseFragment() {
    private lateinit var viewModel: FAQViewModel

    companion object {
        fun newInstance(arg: Int) = FAQsDetailFragment().apply {
            arguments = Bundle().apply {
                this.putInt(EXTRA_KEY1, arg)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FAQViewModel::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val fqaId = arguments?.getInt(EXTRA_KEY1)
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            fqaId?.let { it1 ->
                                viewModel.getFQADetail(it, it1)

                            }
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

        LayoutTheme330{
            val faq = viewModel.cbFQADetail
            Header(stringResource(R.string.title_faq)) {
                viewController?.popFragment()
            }

            Body(content = faq)
        }
    }
}

@Composable
fun Body(content: MutableState<FAQModel>){
    Column(
        Modifier.background(ColorUtils.white_FFFFFF)
            .padding(horizontal = 16.dp)
    ){
        Text(
            text = "Q.${content.value.question}",
            Modifier
                .padding(top = 16.dp),
            style = textBold18
        )
        Row(
            Modifier.padding(top = 6.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = content.value.createdOn ?: "",
                style = textRegular12
            )
            Text(
                text = "·",
                style = textRegular12
            )
            Text(
                text = "조회수 ${content.value.viewCount}",
                style = textRegular12
            )
        }
        Divider(
            Modifier
                .height(1.dp)
                .background(ColorUtils.gray_00000D)
        )
        Row(Modifier.padding(top = 16.dp)) {
            Text(
                text = "A. ",
                style = textBold18,
            )
            Text(
                text = HtmlCompat.fromHtml(
                    content.value.answer ?: "",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ).toString().replace("\n", ""),
                Modifier.align(Alignment.Bottom),
                style = textRegular12
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun FaqPreview() {
    val content = mutableStateOf(FAQModel().apply {
            id = 7
            question =  "군인은 현역을 "
            answer = "<p>군인은 현역을 면한 후가 아니면 국무총리로 임명될 수 없다. 대통령은 즉시 이를 공포하여야 한다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 국회의원의 수는 법률로 정하되.</p>"
            status = "ACTIVATED"
            priority = 7
            viewCount = 10
            createdOn ="2021. 10. 18"
    })
    Body(content = content)
}
