package com.nagaja.the330.view.localnewsdetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class LocalNewsDetailRepo(private val apiService: ApiService) {
    suspend fun getListLocalNewsDetail(token: String, id: Int) = flow {
        emit(apiService.getListLocalNewsDetail(token, id))
    }
}