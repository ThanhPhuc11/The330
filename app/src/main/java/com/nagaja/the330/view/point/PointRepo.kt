package com.nagaja.the330.view.point

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class PointRepo(private val apiService: ApiService) {
    suspend fun getPoints(
        token: String,
        page: Int,
        size: Int,
        pointTransactionType: String,
        timeLimit: String,
    ) = flow {
        emit(apiService.getPoints(token, page, size, pointTransactionType, timeLimit))
    }

    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }
}