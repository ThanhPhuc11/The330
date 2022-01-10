package com.nagaja.the330

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.view.language.LangFragment

class MainActivity : AppCompatActivity() {
    lateinit var viewController: ViewController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewController = ViewController(R.id.flContainer, supportFragmentManager, this)

        viewController.pushFragment(
            "first",
            LangFragment.newInstance()
        )
    }
}