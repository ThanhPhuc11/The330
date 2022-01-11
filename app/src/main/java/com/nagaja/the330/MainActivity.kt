package com.nagaja.the330

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.language.LangFragment

class MainActivity : AppCompatActivity() {
    lateinit var viewController: ViewController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewController = ViewController(R.id.flContainer, supportFragmentManager, this)

        viewController.pushFragment(
            ScreenId.SCREEN_LANGUAGE_FIRST,
            LangFragment.newInstance()
        )
    }
}