package com.nagaja.the330

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.login.LoginFragment
import com.nagaja.the330.view.permission.PermissionFragment
import com.nagaja.the330.view.signupinfo.SignupInfoFragment
import com.nagaja.the330.view.term_sns.TermSNSFragment

class MainActivity : AppCompatActivity() {
    lateinit var viewController: ViewController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewController = ViewController(R.id.flContainer, supportFragmentManager, this)

        viewController.pushFragment(
            ScreenId.SCREEN_LOGIN,
            PermissionFragment.newInstance()
        )

        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = true
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}