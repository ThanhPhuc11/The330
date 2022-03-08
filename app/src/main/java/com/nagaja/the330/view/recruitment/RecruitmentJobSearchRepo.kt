package com.nagaja.the330.view.recruitment

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class RecruitmentJobSearchRepo(private val apiService: ApiService) {
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

    suspend fun checkExistJobSearch(token: String) = flow {
        emit(apiService.checkExistJobSearch(token))
    }
}