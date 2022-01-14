package com.nagaja.the330.view.general

import com.nagaja.the330.model.AuthRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class GeneralRepository(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}