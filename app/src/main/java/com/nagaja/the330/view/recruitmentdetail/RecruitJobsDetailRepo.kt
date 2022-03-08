package com.nagaja.the330.view.recruitmentdetail

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class RecruitJobsDetailRepo(private val apiService: ApiService) {
    suspend fun getRecruitJobsDetail(token: String, id: Int) = flow {
        emit(apiService.getRecruitJobsDetail(token, id))
    }
}