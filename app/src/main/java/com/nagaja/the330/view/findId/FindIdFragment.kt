package com.nagaja.the330.view.findId

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.reset_templace.ResetTemplaceShareVM
import com.nagaja.the330.view.resetpassword.InputIDFragment

class FindIdFragment : BaseFragment() {
    private lateinit var viewModel: FindIdVM
    private lateinit var shareViewModel: ResetTemplaceShareVM

    companion object {
        fun newInstance() = FindIdFragment()
    }

    @Composable
    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this@FindIdFragment)[FindIdVM::class.java]
        shareViewModel =
            ViewModelProvider(
                activity?.supportFragmentManager?.findFragmentByTag(
                    ScreenId.SCREEN_FIND_RESET_TEMPLACE
                )!!
            )[ResetTemplaceShareVM::class.java]
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
                        viewModel.findIdByPhone(
                            shareViewModel.nationalNum,
                            shareViewModel.phoneNum,
                            shareViewModel.otp
                        )
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
            Header("") {
                viewController?.popFragment()
            }
            Text(
                stringResource(R.string.find_id),
                color = ColorUtils.gray_222222,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                stringResource(R.string.your_id_is, viewModel.stateID.value),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 80.dp, bottom = 40.dp),
                color = ColorUtils.gray_222222,
                fontSize = 16.sp
            )
            //TODO: login
            Box(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = ColorUtils.blue_2177E4, shape = RoundedCornerShape(4.dp))
                    .noRippleClickable {
                        //TODO: Login
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.login),
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