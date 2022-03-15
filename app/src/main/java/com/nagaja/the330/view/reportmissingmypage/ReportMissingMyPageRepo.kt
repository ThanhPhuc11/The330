package com.nagaja.the330.view.reportmissingmypage

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ReportMissingMyPageRepo(private val apiService: ApiService) {
    suspend fun getReportMissingMyPage(
        token: String,
        page: Int,
        size: Int,
        timeLimit: String,
        type: String?,
        status: String?,
        all: String?
    ) = flow {
        emit(apiService.getReportMissingMyPage(token, page, size, timeLimit, type, status, all))
    }
}