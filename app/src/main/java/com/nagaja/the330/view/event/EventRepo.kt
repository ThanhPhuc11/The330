package com.nagaja.the330.view.event

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class EventRepo(private val apiService: ApiService) {
    suspend fun getEvent(token: String, page: Int, size: Int) = flow {
        emit(apiService.getEvent(token, page, size))
    }
}