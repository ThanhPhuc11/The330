package com.nagaja.the330.view.resetpassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.verify_otp.VerifyOTPFragment

class InputIDFragment : BaseFragment() {
    private lateinit var viewModel: ResetPwVM

    companion object {
        fun newInstance() = InputIDFragment()
    }

    @Composable
    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this@InputIDFragment)[ResetPwVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackNextScreen.observe(viewLifecycleOwner) {
            viewController?.pushFragment(
                ScreenId.SCREEN_VERIFY_OTP,
                VerifyOTPFragment.newInstance()
            )
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(
                    stringResource(R.string.password_reset),
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 80.dp)
                )
                Text(stringResource(R.string.user_id), style = text14_222)
                TextFieldCustom(
                    hint = stringResource(R.string.please_enter_id),
                    textStateId = viewModel.stateIDInput,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(viewModel.stateIdNotFound.value) {
                    Text(
                        stringResource(R.string.your_id_not_exist_please_check_again),
                        color = ColorUtils.pink_FF1E54,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Box(
                    Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = ColorUtils.gray_222222,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .noRippleClickable {
                            viewModel.checkExistByID()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.next),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 18.sp
                    )
                }

                Text(
                    stringResource(R.string.forgot_ID),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}