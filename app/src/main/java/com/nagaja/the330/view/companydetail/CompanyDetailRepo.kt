package com.nagaja.the330.view.companydetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class CompanyDetailRepo(private val apiService: ApiService) {
    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }
}