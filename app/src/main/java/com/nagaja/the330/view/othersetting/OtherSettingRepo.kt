package com.nagaja.the330.view.othersetting

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class OtherSettingRepo(private val apiService: ApiService) {
    suspend fun logout(token: String) = flow {
        emit(apiService.logout(token))
    }
}