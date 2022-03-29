package com.nagaja.the330.view.mypagecompany

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class MyPageCompanyScreenRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }

    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }

    suspend fun reservationOverview(
        token: String,
        userId: Int,
        role: String
    ) = flow {
        emit(apiService.reservationOverview(token, userId = userId, role = role))
    }
}