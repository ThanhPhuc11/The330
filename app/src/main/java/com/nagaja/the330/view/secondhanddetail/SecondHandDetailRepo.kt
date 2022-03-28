package com.nagaja.the330.view.secondhanddetail

import com.nagaja.the330.model.SecondHandModel
import com.nagaja.the330.model.SecondHandRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class SecondHandDetailRepo(private val apiService: ApiService) {
    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }

    suspend fun viewDetailSecondhandPost(token: String, id: Int) = flow {
        emit(apiService.viewDetailSecondhandPost(token, id))
    }

    suspend fun editSecondhandPost(token: String, body: MutableList<SecondHandModel>) = flow {
        emit(apiService.editSecondhandPost(token, body))
    }
}