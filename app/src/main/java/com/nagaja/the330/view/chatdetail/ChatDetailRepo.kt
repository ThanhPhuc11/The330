package com.nagaja.the330.view.chatdetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ChatDetailRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}