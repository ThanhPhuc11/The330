package com.nagaja.the330.view.point

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class PointRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }

    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }
}