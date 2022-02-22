package com.nagaja.the330.view.signupinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.main.MainFragment
import com.nagaja.the330.view.noRippleClickable

class SignupSuccessFragment : BaseFragment() {
    private lateinit var generalViewModel: GeneralViewModel

    companion object {
        fun newInstance() = SignupSuccessFragment()
    }

    @Composable
    override fun SetupViewModel() {
        generalViewModel =
            getViewModelProvider(this@SignupSuccessFragment)[GeneralViewModel::class.java]
        viewController = (activity as MainActivity).viewController

        generalViewModel.callbackUserDetails.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setUserDetail(it)
            viewController?.pushFragment(
                ScreenId.SCREEN_MAIN,
                MainFragment.newInstance()
            )
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        backSystemHandler {}
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .background(ColorUtils.white_FFFFFF)
                .noRippleClickable { },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.ic_regis_success), contentDescription = null)
            Text(
                stringResource(R.string.member_register_completed),
                color = ColorUtils.gray_222222,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                stringResource(R.string.welcome_to_nagaja_membership),
                color = ColorUtils.gray_626262,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Box(
                Modifier
                    .padding(top = 37.dp)
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ColorUtils.gray_222222)
                    .noRippleClickable {
                        accessToken?.let { generalViewModel.getUserDetails(it) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.go_to_main_page),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}