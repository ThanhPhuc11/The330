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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.nagaja.the330.view.reset_templace.ResetTemplaceShareVM

class ResetPwFragment : BaseFragment() {
    private lateinit var viewModel: ResetPwVM
    private lateinit var shareViewModel: ResetTemplaceShareVM

    companion object {
        fun newInstance() = ResetPwFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ResetPwVM::class.java]
        shareViewModel =
            ViewModelProvider(
                activity?.supportFragmentManager?.findFragmentByTag(
                    ScreenId.SCREEN_FIND_RESET_TEMPLACE
                )!!
            )[ResetTemplaceShareVM::class.java]

        viewController = (activity as MainActivity).viewController

        viewModel.callbackChangeSuccess.observe(viewLifecycleOwner) {
            showMess(getString(R.string.please_login_with_changed_password))
            viewController?.popToFragment(ScreenId.SCREEN_LOGIN)
        }
        viewModel.callbackChangeFail.observe(viewLifecycleOwner) {
            showMessDEBUG("Fail")
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        viewModel.notEntered = stringResource(R.string.please_enter_password)
        viewModel.notValidate = stringResource(R.string.guide_input_password)
        viewModel.notMatch = stringResource(R.string.password_do_not_match_please_check_again)

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
                //TODO: Password
                Text(stringResource(R.string.new_password), style = text14_222)
                Text(
                    stringResource(R.string.guide_input_password),
                    color = ColorUtils.gray_222222,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.please_enter_password),
                    textStateId = viewModel.stateEdtPw
                )
                AnimatedVisibility(viewModel.statePwError.value.isNotEmpty()) {
                    Text(
                        viewModel.statePwError.value,
                        color = ColorUtils.pink_FF1E54,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                //TODO: Re-password
                Text(
                    stringResource(R.string.new_password_confirm),
                    style = text14_222,
                    modifier = Modifier.padding(top = 30.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.please_re_enter_your_password),
                    textStateId = viewModel.stateEdtRePw,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(viewModel.stateRePwError.value.isNotEmpty()) {
                    Text(
                        viewModel.stateRePwError.value,
                        color = ColorUtils.pink_FF1E54,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                //TODO: Button
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
                            viewModel.checkMatchPassword(
                                name = shareViewModel.name,
                                nationNum = shareViewModel.nationalNum,
                                phone = shareViewModel.phoneNum,
                                otp = shareViewModel.otp
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.confirm),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}