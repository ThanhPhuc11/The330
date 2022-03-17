package com.nagaja.the330.view.searchmain

import com.nagaja.the330.model.SecondHandRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class SearchMainRepo(private val apiService: ApiService) {
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

    suspend fun getFreeNoticeBoard(token: String, page: Int, size: Int, sort: String, all: String?) = flow {
        emit(apiService.getFreeNoticeBoard(token, page, size, sort, all))
    }

    suspend fun getRecruitmentList(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        type: String,
        all: String?
    ) = flow {
        emit(apiService.getRecruitmentList(token, page, size, sort, type, all))
    }
}