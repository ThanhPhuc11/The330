package com.nagaja.the330.view.favoritecompany

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class FavCompanyRepo(private val apiService: ApiService) {
    suspend fun getFavoriteCompany(token: String, page: Int, size: Int, sort: String) = flow {
        emit(apiService.getFavoriteCompany(token, page, size, sort))
    }

    suspend fun followCompany(token: String, targetId: Int) = flow {
        emit(apiService.followCompany(token, targetId))
    }

    suspend fun unfollowCompany(token: String, targetId: Int) = flow {
        emit(apiService.unfollowCompany(token, targetId))
    }
}