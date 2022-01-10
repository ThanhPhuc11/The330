package com.nagaja.the330.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder

abstract class BaseActivity : ComponentActivity() {

    //    abstract var viewController: ViewController
    var configurationChangedListener: ((Configuration) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewController =
//            ViewController(getFragmentContainerId(), supportFragmentManager, applicationContext)
    }

    fun getViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            this,
            ViewModelFactory(
                RetrofitBuilder.getInstance(this)
                    ?.create(ApiService::class.java)!!
            )
        )
    }

//    open fun getAccessToken(): String {
//        return formatToken(AppPreferencesHelper(this).authToken.accessToken)
//    }

//    open fun formatToken(token: String?): String {
//        if (TextUtils.isEmpty(token)) {
////            showMess("Token Empty!")
//            return ""
//        }
//        return "Bearer $token"
//    }

    open fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

//    override fun onBackPressed() {
//        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
//            super.onBackPressed()
//        } else {
//            viewController.popFragment()
//        }
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.d(
            "BaseActivity",
            "Configuration Changed: \n Window size ${displayMetrics.widthPixels}x${displayMetrics.heightPixels}"
        )
        configurationChangedListener?.invoke(newConfig)
    }
}