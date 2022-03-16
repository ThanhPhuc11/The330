package com.nagaja.the330.view.secondhandmypage

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class SecondHandMypageRepo(private val apiService: ApiService) {
    suspend fun getMySecondHand(token: String, page: Int, size: Int, sort: String?, transactionStatus: String?) = flow {
        emit(apiService.getMySecondHand(token, page, size, sort, transactionStatus))
    }
}