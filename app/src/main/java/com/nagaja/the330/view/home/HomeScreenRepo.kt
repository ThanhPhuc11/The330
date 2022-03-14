package com.nagaja.the330.view.home

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class HomeScreenRepo(private val apiService: ApiService) {
    suspend fun getCategory(token: String, group: String?) = flow {
        emit(apiService.getCategory(token, group))
    }

    suspend fun getCompanyRecommendAds(token: String) = flow {
        emit(apiService.getCompanyRecommendAds(token))
    }

}