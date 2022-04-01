package com.nagaja.the330.view.termservice

import com.nagaja.the330.model.PhoneAvailableModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class TermServiceRepo(private val apiService: ApiService) {
    suspend fun getConfigInfo(token: String, type: String) = flow {
        emit(apiService.getConfigInfo(token, type))
    }
}