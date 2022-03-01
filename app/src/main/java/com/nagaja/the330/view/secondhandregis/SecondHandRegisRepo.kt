package com.nagaja.the330.view.secondhandregis

import com.nagaja.the330.model.SecondHandRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class SecondHandRegisRepo(private val apiService: ApiService) {
    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }

    suspend fun makeSecondhandPost(token: String, body: SecondHandRequest) = flow {
        emit(apiService.makeSecondhandPost(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }
}