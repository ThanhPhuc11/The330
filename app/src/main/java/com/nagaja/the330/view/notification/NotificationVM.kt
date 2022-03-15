package com.nagaja.the330.view.notification

import androidx.compose.runtime.mutableStateListOf
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.NotificationModel

class NotificationVM(private val repo: NotificationRepo) : BaseViewModel(){
    val noticeStateList = mutableStateListOf<NotificationModel>()
}