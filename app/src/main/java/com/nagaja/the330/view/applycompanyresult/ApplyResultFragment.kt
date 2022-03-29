package com.nagaja.the330.view.applycompanyresult

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.applycompany.ApplyCompanyFragment
import com.nagaja.the330.view.noRippleClickable

class ApplyResultFragment : BaseFragment() {
    private lateinit var viewModel: ApplyResultVM

    companion object {
        fun newInstance(justApply: Boolean = true) = ApplyResultFragment().apply {
            arguments = Bundle().apply {
                putBoolean(AppConstants.EXTRA_KEY1, justApply)
            }
        }
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
                    Lifecycle.Event.ON_CREATE -> {
                        viewModel.getUserDetails(accessToken!!)
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
            val text = remember { mutableStateOf("") }
            val isShowReason = remember { mutableStateOf(false) }
            if (requireArguments().getBoolean(AppConstants.EXTRA_KEY1)) {
                text.value = "신청 완료"
                isShowReason.value = false
            } else {
                if (viewModel.stateUserDetail.value.companyRequest?.status == "DELETED") {
                    text.value = "반려"
                    isShowReason.value = true
                } else {
                    text.value = "신청 완료"
                    isShowReason.value = false
                }
            }
            Text(
                "${
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_24,
                        viewModel.stateUserDetail.value.companyRequest?.createdOn ?: ""
                    )
                } 신청하신 \n" +
                        "기업회원 등록이 ${text.value} 되었습니다.",
                color = ColorUtils.black_000000,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (isShowReason.value)
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
                            "${viewModel.stateUserDetail.value.companyRequest?.rejectReason}",
                            color = ColorUtils.black_000000,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f))

                    Box(
                        Modifier
                            .padding(vertical = 16.dp)
                            .height(52.dp)
                            .fillMaxWidth()
                            .background(ColorUtils.blue_2177E4)
                            .noRippleClickable {
                                viewController?.pushFragment(
                                    ScreenId.SCREEN_APPLY_COMPANY,
                                    ApplyCompanyFragment.newInstance()
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "기업회원 재신청",
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
        }
    }
}