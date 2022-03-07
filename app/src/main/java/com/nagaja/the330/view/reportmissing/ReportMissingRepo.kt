package com.nagaja.the330.view.reportmissing

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ReportMissingRepo(private val apiService: ApiService) {
    suspend fun getReportMissingList(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        type: String,
        all: String?
    ) = flow {
        emit(apiService.getReportMissingList(token, page, size, sort, type, all))
    }
}