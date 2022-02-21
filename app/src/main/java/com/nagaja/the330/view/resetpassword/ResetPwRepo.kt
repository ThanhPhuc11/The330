package com.nagaja.the330.view.resetpassword

import com.nagaja.the330.model.PhoneAvailableModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ResetPwRepo(private val apiService: ApiService) {
    suspend fun checkExistByID(body: UserDetail) = flow {
        emit(apiService.checkExistByID(body))
    }
}