package com.nagaja.the330.view.termservice

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class TermServiceFragment : BaseFragment() {
    private lateinit var viewModel: TermServiceVM

    companion object {
        fun newInstance(type: String) = TermServiceFragment().apply {
            arguments = Bundle().apply {
                putString(AppConstants.EXTRA_KEY1, type)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[TermServiceVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            viewModel.getConfigInfo(
                                it,
                                requireArguments().getString(
                                    AppConstants.EXTRA_KEY1,
                                    "TERM_SERVICE"
                                )
                            )
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {

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
            Header(
                when (requireArguments().getString(AppConstants.EXTRA_KEY1, "TERM_SERVICE")) {
                    "TERM_SERVICE" -> "서비스 이용약관"
                    "PRIVACY_POLICY" -> "개인정보 처리방침"
                    "LOCATION_TERM" -> "위치정보 동의 약관"
                    else -> ""
                }
            ) {
                viewController?.popFragment()
            }

            Divider(
                color = ColorUtils.gray_E1E1E1,
                modifier = Modifier
                    .padding(16.dp)
            )
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    HtmlCompat.fromHtml(
                        viewModel.stateDataConfig.value.value ?: "",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString(),
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                )
            }
        }
    }
}