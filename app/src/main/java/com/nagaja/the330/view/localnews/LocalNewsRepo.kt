package com.nagaja.the330.view.localnews

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class LocalNewsRepo(private val apiService: ApiService) {
    suspend fun getListLocalNews(token: String, page: Int, size: Int, sort: String, all: String?) = flow {
        emit(apiService.getListLocalNews(token, page, size, sort, all))
    }
}