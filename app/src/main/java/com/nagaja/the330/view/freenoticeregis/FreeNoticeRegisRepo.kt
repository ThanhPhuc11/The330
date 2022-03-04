package com.nagaja.the330.view.freenoticeregis

import com.nagaja.the330.model.FreeNoticePostRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class FreeNoticeRegisRepo(private val apiService: ApiService) {
    suspend fun makePostNotice(token: String, body: FreeNoticePostRequest) = flow {
        emit(apiService.makePostNotice(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }
}