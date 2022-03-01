package com.nagaja.the330.view.secondhandregis

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class SecondHandRegisRepo(private val apiService: ApiService) {
    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }
}