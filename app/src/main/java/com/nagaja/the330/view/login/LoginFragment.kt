package com.nagaja.the330.view.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
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
import com.nagaja.the330.view.reset_templace.ResetTemplaceFragment
import com.nagaja.the330.view.resetpassword.InputIDFragment
import com.nagaja.the330.view.signupinfo.GoogleMapFragment
import com.nagaja.the330.view.signupinfo.SignupInfoFragment
import com.nagaja.the330.view.text14_222
import com.nagaja.the330.view.text14_62
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.RequestBody

class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var generalViewModel: GeneralViewModel
    private lateinit var mOAuthLoginModule: OAuthLogin
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager //Facebook callback
    private var userType = AppConstants.GENERAL

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun SetupViewModel() {
//        val viewModelStoreOwner: ViewModelStoreOwner =
//            checkNotNull(LocalViewModelStoreOwner.current) {
//                "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
//            }
        viewModel = getViewModelProvider(this)[LoginViewModel::class.java]
        generalViewModel = getViewModelProvider(this)[GeneralViewModel::class.java]

        viewController = (activity as MainActivity).viewController

        viewModel.callbackLoginKaKaoSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { token -> generalViewModel.getUserDetails("Bearer $token") }
        }
        viewModel.callbackLoginNaverSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { token -> generalViewModel.getUserDetails("Bearer $token") }
        }
        viewModel.callbackLoginGGSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { token -> generalViewModel.getUserDetails("Bearer $token") }
        }
        viewModel.callbackLoginFbSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { token -> generalViewModel.getUserDetails("Bearer $token") }
        }
        viewModel.callbackLoginIdSuccess.observe(viewLifecycleOwner) {
            DataStorePref(requireContext()).setToken(it)
            it.accessToken?.let { token -> generalViewModel.getUserDetails("Bearer $token") }
        }
        generalViewModel.callbackUserDetails.observe(viewLifecycleOwner) {
            if (it.agreePolicy == true) {
                DataStorePref(requireContext()).setUserDetail(it)
                viewController?.pushFragment(
                    ScreenId.SCREEN_MAIN,
                    MainFragment.newInstance()
                )
            } else {
                viewController?.pushFragment(
                    ScreenId.SCREEN_SIGNUP_MAIL,
                    SignupInfoFragment.newInstance(viewModel.snsType.isNotEmpty())
                )
            }
            viewModel.snsType = ""
        }
    }

    @Preview(showBackground = true)
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        //KaKao
                        KakaoSdk.init(requireContext(), getString(R.string.kakao_native_app_key))

                        //Naver
                        mOAuthLoginModule = OAuthLogin.getInstance()
                        mOAuthLoginModule.init(
                            activity,
                            getString(R.string.naver_client_id),
                            getString(R.string.naver_client_secret),
                            "나가자 nagaja"
                        )
                        //GG
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
                            .requestServerAuthCode(getString(R.string.google_server_client_id))
                            .requestEmail()
                            .build()
                        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

                        //Fb
                        callbackManager = CallbackManager.Factory.create()
                        FacebookSdk.sdkInitialize(activity?.applicationContext)
                        AppEventsLogger.activateApp(activity?.application)
                        LoginManager.getInstance().registerCallback(callbackManager,
                            object : FacebookCallback<LoginResult?> {
                                override fun onSuccess(loginResult: LoginResult?) {
                                    // App code
                                    Log.e("Facebook", "successs")
                                    viewModel.loginWithFb(
                                        loginResult?.accessToken?.token.toString(),
                                        userType
                                    )
                                }

                                override fun onCancel() {
                                    Log.e("Facebook", "cancel")
                                }

                                override fun onError(exception: FacebookException) {
                                    Log.e("Facebook", "error")
                                }
                            })
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
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
                modifier = Modifier
                    .padding(top = screenHeight / 10)
                    .noRippleClickable {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_GOOGLE_MAP,
                            GoogleMapFragment.newInstance().apply {
                                callbackLocation = { lat, long ->
                                    showMessDEBUG("$lat - $long")
                                }
                            }
                        )
                    }
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
                Text(
                    stringResource(R.string.find_id),
                    style = text14_62,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_FIND_RESET_TEMPLACE,
                                ResetTemplaceFragment.newInstance()
                            )
                        })
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                Text(
                    stringResource(R.string.find_password),
                    style = text14_62,
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
//                            viewController?.pushFragment(
//                                ScreenId.SCREEN_RESET_INPUT_ID,
//                                InputIDFragment.newInstance()
//                            )
                        })
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                Text(
                    stringResource(R.string.signup),
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.signup_for_membership_sns_account),
                style = text14_222
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
                        .noRippleClickable {
                            mGoogleSignInClient.signOut()
                            snsGoogleLogin(mGoogleSignInClient)
                        }
                )
                IconLogin(ColorUtils.blue_3B5998, R.drawable.ic_facebook) {
                    LoginManager.getInstance().logOut()
                    LoginManager.getInstance()
                        .logInWithReadPermissions(
                            this@LoginFragment,
                            mutableListOf("public_profile")
                        )
                }
            }
            Text(
                stringResource(R.string.use_as_a_non_member),
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

    private fun snsGoogleLogin(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startForResultCallback.launch(signInIntent)
    }

    private val startForResultCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val ggResult =
                    data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
                handleGoogleSignInResult(ggResult!!)
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
        val acct: GoogleSignInAccount? = result.signInAccount
        val authCode = acct?.serverAuthCode

        val requestBody: RequestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add(
                "client_id",
                getString(R.string.google_server_client_id)
            )
            .add(
                "client_secret",
                getString(R.string.google_server_client_secret)
            )
            .add("redirect_uri", "")
            .add("code", "$authCode")
            .add("id_token", "${acct?.idToken}")
            .build()
        viewModel.loginGoogleAuth2(requestBody, userType)
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