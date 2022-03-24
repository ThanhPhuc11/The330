package com.nagaja.the330.view.recruimentcompany

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class RecruitmentCompanyRepo(private val apiService: ApiService) {
    suspend fun getRecruitmentMypage(token: String, page: Int, size: Int, timeLimit: String) = flow {
        emit(apiService.getRecruitmentMypage(token, page, size, timeLimit))
    }
    suspend fun getChatList(
        token: String,
        page: Int,
        size: Int,
        type: String?,
        typeSearchChat: String?,
    ) = flow {
        emit(apiService.getChatList(token, page, size, type, typeSearchChat))
    }
}