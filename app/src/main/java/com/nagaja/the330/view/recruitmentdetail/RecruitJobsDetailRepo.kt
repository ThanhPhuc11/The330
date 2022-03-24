package com.nagaja.the330.view.recruitmentdetail

import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class RecruitJobsDetailRepo(private val apiService: ApiService) {
    suspend fun getRecruitJobsDetail(token: String, id: Int) = flow {
        emit(apiService.getRecruitJobsDetail(token, id))
    }

    suspend fun editPostRecruitJobs(token: String, body: RecruitmentJobsModel) = flow {
        emit(apiService.editPostRecruitJobs(token, body))
    }

    suspend fun checkConfirm(token: String, recruitmentJobId: Int) = flow {
        emit(apiService.checkConfirm(token, recruitmentJobId))
    }

    suspend fun confirmRecruit(token: String, recruitmentJobId: Int) = flow {
        emit(apiService.confirmRecruit(token, recruitmentJobId))
    }
}