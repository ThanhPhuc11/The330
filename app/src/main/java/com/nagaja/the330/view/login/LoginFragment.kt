package com.nagaja.the330.view.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.main.MainFragment
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.signupinfo.SignupInfoFragment
import com.nagaja.the330.view.text14_62
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var generalViewModel: GeneralViewModel
    private lateinit var mOAuthLoginModule: OAuthLogin
    private var userType = AppConstants.GENERAL

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    @Composable
    override fun SetupViewModel() {
        val viewModelStoreOwner: ViewModelStoreOwner =
            checkNotNull(LocalViewModelStoreOwner.current) {
                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
            }
        viewModel = getViewModelProvider(viewModelStoreOwner)[LoginViewModel::class.java]
        generalViewModel = getViewModelProvider(viewModelStoreOwner)[GeneralViewModel::class.java]

        viewController = (activity as MainActivity).viewController

        viewModel.callbackLoginKaKaoSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { generalViewModel.getUserDetails(accessToken!!) }
        }
        viewModel.callbackLoginIdSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { generalViewModel.getUserDetails(accessToken!!) }
        }
        generalViewModel.callbackUserDetails.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setUserDetail(it)
            viewController?.pushFragment(
                ScreenId.SCREEN_MAIN,
                MainFragment.newInstance()
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    override fun UIData() {
        LayoutTheme330(
            Modifier
                .background(ColorUtils.white_FFFFFF)
                .padding(16.dp)
        ) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            Text(
                stringResource(R.string.login),
                color = ColorUtils.gray_222222,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = screenHeight / 10)
            )

            //TODO: login
            Text(
                stringResource(R.string.user_id),
                color = ColorUtils.gray_222222,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 44.dp),
                fontWeight = FontWeight.SemiBold
            )
//            val textStateId = remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = viewModel.edtId.value,
                onValueChange = { if (it.text.length <= 20) viewModel.edtId.value = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.please_enter_id),
                        fontSize = 14.sp,
                        color = ColorUtils.gray_BEBEBE
                    )
                },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .background(ColorUtils.white_FFFFFF)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    ),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ColorUtils.white_FFFFFF,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            //TODO: password
            Text(
                stringResource(R.string.password),
                color = ColorUtils.gray_222222,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 24.dp),
                fontWeight = FontWeight.SemiBold
            )
            TextField(
                value = viewModel.edtPw.value,
                onValueChange = { if (it.text.length <= 15) viewModel.edtPw.value = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.please_enter_password),
                        fontSize = 14.sp,
                        color = ColorUtils.gray_BEBEBE
                    )
                },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .background(ColorUtils.white_FFFFFF)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    ),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ColorUtils.white_FFFFFF,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            AnimatedVisibility(
                visible = viewModel.textError.value != null,
                Modifier.padding(top = 20.dp)
            ) {
                viewModel.textError.value?.let {
                    Text(
                        stringResource(it),
                        fontSize = 12.sp,
                        color = ColorUtils.pink_FF1E54
                    )
                }
            }

            //TODO: Button Login
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {
                        viewModel.checkLogin()
                    }
            ) {
                Text(
                    stringResource(R.string.login),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 18.sp
                )
            }
            Row(Modifier.padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("아이디 찾기", style = text14_62, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                Text("비밀번호 찾기", style = text14_62, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                Text(
                    "회원가입",
                    style = text14_62,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_SIGNUP_MAIL,
                                SignupInfoFragment.newInstance()
                            )
                        })
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginSocial()
        }
    }

    @Composable
    fun LoginSocial() {
        //KaKao
        val context = LocalContext.current
        KakaoSdk.init(context, getString(R.string.kakao_native_app_key))

        //Naver
        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            activity,
            stringResource(R.string.naver_client_id),
            stringResource(R.string.naver_client_secret),
            "나가자 nagaja"
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "SNS 계정으로 회원가입/로그인",
                fontSize = 14.sp,
                color = ColorUtils.gray_222222
            )
            Row(Modifier.padding(top = 20.dp)) {
                IconLogin(ColorUtils.yellow_FFCD00, R.drawable.ic_kakao) {
                    snsKakaoLogin()
                }
                IconLogin(ColorUtils.green_1EC800, R.drawable.ic_naver) {
                    newSnsNaverLogin(true)
                }
                Image(
                    painter = painterResource(R.drawable.ic_google_login),
                    contentDescription = "",
                    Modifier
                        .padding(8.dp)
                        .size(48.dp)
                        .clickable {

                        }
                )
                IconLogin(ColorUtils.blue_3B5998, R.drawable.ic_facebook, null)
            }
            Text(
                "비회원으로 이용하기",
                color = ColorUtils.gray_626262,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 37.dp)
            )
        }
    }

    @Composable
    fun IconLogin(color: Color, drawable: Int, snsLogin: (() -> Unit)? = null) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .size(48.dp)
                .background(color = color)
                .clickable {
                    snsLogin?.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(drawable), contentDescription = "")
        }
    }

    private fun snsKakaoLogin() {
        LoginClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            viewModel.loginWithKakao(token.accessToken, userType)
        }
    }

    private fun newSnsNaverLogin(otherAcc: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (otherAcc) {
                    mOAuthLoginModule.logoutAndDeleteToken(context)
                }
                Log.e("PHUC", mOAuthLoginModule.getState(activity).toString())
                when (mOAuthLoginModule.getState(activity)) {
                    OAuthLoginState.OK -> {
                        snsNaverLogin()
                    }
                    OAuthLoginState.NEED_LOGIN -> {
                        snsNaverLogin()
                    }
                    OAuthLoginState.NEED_REFRESH_TOKEN -> {
                        snsNaverLogin()
                    }
                    else -> {
                        Log.e("PHUC", "ERROR")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun snsNaverLogin() {
        mOAuthLoginModule.startOauthLoginActivity(activity, mNaverOAuthLoginHandler)
    }

    private val mNaverOAuthLoginHandler: OAuthLoginHandler = @SuppressLint("HandlerLeak")
    object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken = mOAuthLoginModule.getAccessToken(context)
                val refreshToken = mOAuthLoginModule.getRefreshToken(context)
                val expiresAt = mOAuthLoginModule.getExpiresAt(context)
                val tokenType = mOAuthLoginModule.getTokenType(context)

                viewModel.loginWithNaver(accessToken, userType)
            } else {
                val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
            }
        }
    }
}