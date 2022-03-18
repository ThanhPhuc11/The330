package com.nagaja.the330.view.companydetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class CompanyDetailRepo(private val apiService: ApiService) {
    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }

    suspend fun followCompany(token: String, targetId: Int) = flow {
        emit(apiService.followCompany(token, targetId))
    }

    suspend fun unfollowCompany(token: String, targetId: Int) = flow {
        emit(apiService.unfollowCompany(token, targetId))
    }

    suspend fun checkFollowCompany(token: String, targetId: Int) = flow {
        emit(apiService.checkFollowCompany(token, targetId))
    }
}