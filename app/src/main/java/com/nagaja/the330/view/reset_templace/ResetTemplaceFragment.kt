package com.nagaja.the330.view.reset_templace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.findId.FindIdFragment
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.resetpassword.InputIDFragment
import com.nagaja.the330.view.verify_otp.VerifyOTPFragment

class ResetTemplaceFragment : BaseFragment() {
    private lateinit var shareViewModel: ResetTemplaceShareVM

    companion object {
        fun newInstance() = ResetTemplaceFragment()
    }

    @Composable
    override fun SetupViewModel() {
        shareViewModel =
            ViewModelProvider(
                this
            )[ResetTemplaceShareVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            Text(
                stringResource(R.string.reset_id_and_password),
                color = ColorUtils.gray_222222,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp)
            )

            //TODO: Find ID
            Box(
                modifier = Modifier
                    .padding(top = 80.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = ColorUtils.blue_2177E4, shape = RoundedCornerShape(4.dp))
                    .noRippleClickable {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_VERIFY_OTP,
                            VerifyOTPFragment
                                .newInstance()
                                .apply {
                                    callbackResult = { isSuccess, nationNum, phoneNum, otp ->
                                        if (isSuccess) {
                                            shareViewModel.nationalNum = nationNum
                                            shareViewModel.phoneNum = phoneNum
                                            shareViewModel.otp = otp

                                            viewController?.pushFragment(
                                                ScreenId.SCREEN_FIND_ID,
                                                FindIdFragment.newInstance()
                                            )
                                        }
                                    }
                                }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.find_id),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            //TODO: Reset Password
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_222222,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .noRippleClickable {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_RESET_INPUT_ID,
                            InputIDFragment.newInstance()
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.password_reset),
                    color = ColorUtils.gray_222222,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}