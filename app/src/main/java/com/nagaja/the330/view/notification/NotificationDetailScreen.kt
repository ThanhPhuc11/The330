package com.nagaja.the330.view.notification

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.view.LayoutTheme330

class NotificationDetailScreen: BaseFragment() {
    private lateinit var viewModel: NotificationVM

    companion object {
        fun newInstance(arg: Int) = NotificationDetailScreen().apply {
            arguments = Bundle().apply {
                this.putInt(AppConstants.EXTRA_KEY1, arg)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[NotificationVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val noticeId = arguments?.getInt(AppConstants.EXTRA_KEY1)
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            noticeId?.let { it1 ->

                            }
                        }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        LayoutTheme330{
            Box(
                Modifier.background(Color.White)
            )
        }
    }
}