package com.nagaja.the330.view.signupinfo

import com.nagaja.the330.model.PhoneAvailableModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class SignupInfoRepo(private val apiService: ApiService) {
    suspend fun checkExistPhone(body: PhoneAvailableModel) = flow {
        emit(apiService.checkExistPhone(body))
    }

    suspend fun sendPhone(body: PhoneAvailableModel) = flow {
        emit(apiService.sendPhone(body))
    }

    suspend fun sendOTP(body: PhoneAvailableModel) = flow {
        emit(apiService.sendOTP(body))
    }
}