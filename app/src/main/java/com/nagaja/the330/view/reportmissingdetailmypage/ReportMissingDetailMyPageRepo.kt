package com.nagaja.the330.view.reportmissingdetailmypage

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ReportMissingDetailMyPageRepo(private val apiService: ApiService) {
    suspend fun getReportMissingDetail(token: String, id: Int) = flow {
        emit(apiService.getReportMissingDetail(token, id))
    }
}