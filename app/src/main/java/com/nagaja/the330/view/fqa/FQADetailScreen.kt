package com.nagaja.the330.view.fqa

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.nagaja.the330.ui.theme.textBold18
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.AppConstants.EXTRA_KEY1
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class FQAsDetailScreen: BaseFragment() {
    private lateinit var viewModel: FQAViewModel

    companion object {
        fun newInstance(arg: Int) = FQAsDetailScreen().apply {
            arguments = Bundle().apply {
                this.putInt(EXTRA_KEY1, arg)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[FQAViewModel::class.java]
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
            val fqa = viewModel.cbFQADetail
            Header(stringResource(R.string.title_fqa)) {
                viewController?.popFragment()
            }

            Column{
                Text(
                    text = "Q.${fqa.value.question}",
                    Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    style = textBold18
                )
                Row(
                    Modifier
                        .padding(top = 6.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = fqa.value.createdOn ?: "",
                        style = textRegular12
                    )
                    Text(
                        text = ".",
                        style = textRegular12
                    )
                    Text(
                        text = "조회수 ${fqa.value.viewCount}",
                        style = textRegular12
                    )
                }
                Divider(
                    Modifier
                        .height(1.dp)
                        .background(ColorUtils.gray_00000D)
                        .padding(16.dp)
                )
                Row(Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "A. ",
                        style = textBold18,
                    )
                    Text(
                        text = HtmlCompat.fromHtml(
                            fqa.value.answer ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString().replace("\n", ""),
                        Modifier.align(Alignment.Bottom),
                        style = textRegular12
                    )
                }
            }
        }
    }
}