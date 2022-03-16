package com.nagaja.the330.view.chatlist

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ChatListRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}