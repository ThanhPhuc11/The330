package com.nagaja.the330.view.localnewsdetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class LocalNewsDetailRepo(private val apiService: ApiService) {
    suspend fun getLocalNewsDetail(token: String, id: Int) = flow {
        emit(apiService.getLocalNewsDetail(token, id))
    }
}