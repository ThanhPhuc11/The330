package com.nagaja.the330.view.chatlist

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ChatListRepo(private val apiService: ApiService) {
    suspend fun getChatList(
        token: String,
        page: Int,
        size: Int,
        type: String?,
        startTime: String?,
        endTime: String?
    ) = flow {
        emit(apiService.getChatList(token, page, size, type, startTime, endTime))
    }
}