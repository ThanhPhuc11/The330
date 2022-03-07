package com.nagaja.the330.view.secondhandmypage

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class SecondHandMypageRepo(private val apiService: ApiService) {
    suspend fun getFavoriteCompany(token: String, page: Int, size: Int, sort: String) = flow {
        emit(apiService.getFavoriteCompany(token, page, size, sort))
    }
}