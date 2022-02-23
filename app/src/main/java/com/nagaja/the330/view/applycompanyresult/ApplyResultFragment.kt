package com.nagaja.the330.view.applycompanyresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class ApplyResultFragment : BaseFragment() {
    private lateinit var viewModel: ApplyResultVM

    companion object {
        fun newInstance() = ApplyResultFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ApplyResultVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {

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
            Header(title = stringResource(R.string.apply_company_result_title)) {
                viewController?.popFragment()
            }
            Text(
                "yyyy년 mm월 dd일 신청하신 \n" +
                        "기업회원 등록이 [신청 완료/ 등록 완료/ 반려] 되었습니다.",
                color = ColorUtils.black_000000,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column(
                Modifier
                    .padding(top = 50.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("반려사유", color = ColorUtils.black_000000, fontSize = 18.sp)
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .background(ColorUtils.gray_F5F5F5)
                        .padding(20.dp)
                ) {
                    Text(
                        "{Admin에서 등록한 승인 거절 사유 노출\n" +
                                "0000한 이유로 NAGAGA에 기업 신청이 반려 되었습니다. 0000한 이유로 NAGAGA에 기업 신청이 반려 되었습니다. 0000한 이유로 NAGAGA에 기업 신청이 반려 되었습니다. }",
                        color = ColorUtils.black_000000,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}