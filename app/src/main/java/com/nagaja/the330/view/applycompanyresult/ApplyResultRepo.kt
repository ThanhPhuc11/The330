package com.nagaja.the330.view.applycompanyresult

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ApplyResultRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}