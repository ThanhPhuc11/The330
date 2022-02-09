package com.nagaja.the330.view.mypage

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class MyPageScreenRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}