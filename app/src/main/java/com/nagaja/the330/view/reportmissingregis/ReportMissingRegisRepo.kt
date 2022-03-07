package com.nagaja.the330.view.reportmissingregis

import com.nagaja.the330.model.FreeNoticePostRequest
import com.nagaja.the330.model.ReportMissingModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class ReportMissingRegisRepo(private val apiService: ApiService) {
    suspend fun makePostReportMissing(token: String, body: ReportMissingModel) = flow {
        emit(apiService.makePostReportMissing(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }
}