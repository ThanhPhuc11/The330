package com.nagaja.the330.view.faq

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class FAQRepo(private val apiService: ApiService) {
    suspend fun getFQAs(token: String) = flow{
        emit(apiService.getFQAs(token))
    }

    suspend fun getFQADetail(token: String, id: Int) = flow{
        emit(apiService.getFQADetail(token = token, id = id))
    }
}