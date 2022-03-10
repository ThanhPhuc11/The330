package com.nagaja.the330.view.companylist

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class CompanyListRepo(private val apiService: ApiService) {
    suspend fun findCompany(
        token: String,
        page: Int,
        size: Int,
        cType: String,
        sort: String,
        cityId: Int?,
        district: Int?,
        all: String?
    ) = flow {
        emit(apiService.findCompany(token, page, size, cType, sort, cityId, district, all))
    }
}