package com.nagaja.the330.view.freenoticeboard

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class FreeNoticeRepo(private val apiService: ApiService) {
    suspend fun getFreeNoticeBoard(token: String, page: Int, size: Int, sort: String, all: String?) = flow {
        emit(apiService.getFreeNoticeBoard(token, page, size, sort, all))
    }
}