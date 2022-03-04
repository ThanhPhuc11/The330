package com.nagaja.the330.view.freenoticedetail

import com.nagaja.the330.model.ReportModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class FreeNoticeDetailRepo(private val apiService: ApiService) {
    suspend fun getFreeNotiDetail(token: String, id: Int) = flow {
        emit(apiService.getFreeNotiDetail(token, id))
    }

    suspend fun reportFreeNotice(token: String, body: ReportModel) = flow {
        emit(apiService.reportFreeNotice(token, body))
    }
}