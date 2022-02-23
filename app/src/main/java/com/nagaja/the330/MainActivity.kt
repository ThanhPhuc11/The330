package com.nagaja.the330

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.general.GeneralViewModel
import com.nagaja.the330.view.login.LoginFragment
import com.nagaja.the330.view.main.MainFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {
    lateinit var viewController: ViewController
    lateinit var generalViewModel: GeneralViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewController = ViewController(R.id.flContainer, supportFragmentManager, this)
        generalViewModel = getViewModelProvider()[GeneralViewModel::class.java]

        checkOrFakeToken()

        WindowCompat.setDecorFitsSystemWindows(window, true)
        ViewCompat.getWindowInsetsController(window.decorView).let { controller ->
            controller?.isAppearanceLightStatusBars = true
            controller?.show(WindowInsetsCompat.Type.systemBars())
            controller?.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
//        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
//            controller.isAppearanceLightStatusBars = true
//            controller.show(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior =
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }

        generalViewModel.callbackUserDetails.observe(this) {
            if (it.agreePolicy == true) {
                DataStorePref(this).setUserDetail(it)
                viewController.pushFragment(
                    ScreenId.SCREEN_MAIN,
                    MainFragment.newInstance()
                )
            } else {
                viewController.pushFragment(
                    ScreenId.SCREEN_LOGIN,
                    LoginFragment.newInstance()
                )
            }
        }
        generalViewModel.callbackUserFail.observe(this) {
            viewController.pushFragment(
                ScreenId.SCREEN_LOGIN,
                LoginFragment.newInstance()
            )
        }
    }

    private fun formatToken(token: String?): String {
        if (TextUtils.isEmpty(token)) {
//            showMess("Token Empty!")
            return ""
        }
        return "Bearer $token"
    }

    private fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            this,
            ViewModelFactory(
                RetrofitBuilder.getInstance(this)
                    ?.create(ApiService::class.java)!!
            )
        )
    }

    private fun checkOrFakeToken() {
        CoroutineScope(Dispatchers.IO).launch {
            var tokenModel: AuthTokenModel?
            dataStore.data.map { get ->
                get[DataStorePref.AUTH_TOKEN] ?: ""
            }.collect {
                tokenModel = Gson().fromJson(it, AuthTokenModel::class.java)
                delay(500)
                withContext(Dispatchers.Main) {
                    if (tokenModel == null) {
                        viewController.pushFragment(
                            ScreenId.SCREEN_LOGIN,
                            LoginFragment.newInstance()
                        )
                        this@launch.coroutineContext.job.cancel()
                    } else {
                        val accessToken = formatToken(tokenModel!!.accessToken)
                        generalViewModel.getUserDetails(accessToken)
                        this@launch.coroutineContext.job.cancel()
                    }
                }
            }
        }
    }
}