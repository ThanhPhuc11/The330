package com.nagaja.the330.view.companylist

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class CompanyListRepo(private val apiService: ApiService) {
    suspend fun findCompany(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        serviceTypes: MutableList<String>?,
        cType: String?,
        authentication: Boolean?,
        cityId: Int?,
        district: Int?,
        all: String?
    ) = flow {
        emit(apiService.findCompany(token, page, size, sort, serviceTypes, cType, authentication, cityId, district, all))
    }

    suspend fun getBanner(token: String, cType: String) = flow {
        emit(apiService.getBannerCompany(token, cType))
    }
}