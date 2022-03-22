package com.nagaja.the330.view.regularcustomer

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class RegularRepo(private val apiService: ApiService) {
    suspend fun getMyLike(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        followType: String?
    ) = flow {
        emit(apiService.getMyFollow(token, page, size, sort, followType))
    }

    suspend fun getLikeMe(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        followType: String?
    ) = flow {
        emit(apiService.getFollowMe(token, page, size, sort, followType))
    }
}