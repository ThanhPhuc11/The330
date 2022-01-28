package com.nagaja.the330.view.login

import com.nagaja.the330.model.AuthRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class LoginRepository(private val apiService: ApiService) {
    suspend fun loginWithKakao(obj: AuthRequest) = flow {
        emit(apiService.authWithKakao(obj))
    }

    suspend fun authWithNaver(obj: AuthRequest) = flow {
        emit(apiService.authWithNaver(obj))
    }

    suspend fun loginWithId(obj: AuthRequest) = flow {
        emit(apiService.loginWithId(obj))
    }
}