package com.nagaja.the330.view.resetpassword

import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ResetPwRepo(private val apiService: ApiService) {
    suspend fun checkExistByID(body: UserDetail) = flow {
        emit(apiService.checkExistByID(body))
    }
    suspend fun changePassword(body: UserDetail) = flow {
        emit(apiService.changePassword(body))
    }
}