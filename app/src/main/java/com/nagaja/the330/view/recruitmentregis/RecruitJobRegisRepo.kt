package com.nagaja.the330.view.recruitmentregis

import com.nagaja.the330.model.FreeNoticePostRequest
import com.nagaja.the330.model.ReportMissingModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class RecruitJobRegisRepo(private val apiService: ApiService) {
    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }

    suspend fun makePostReportMissing(token: String, body: ReportMissingModel) = flow {
        emit(apiService.makePostReportMissing(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }
}