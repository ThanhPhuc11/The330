package com.nagaja.the330.view.comment

import com.nagaja.the330.model.CommentModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class CommentRepo(private val apiService: ApiService) {
    suspend fun getCommentsById(
        token: String,
        page: Int,
        size: Int,
        sort: String,
        status: String?,
        freeNoticeBoardId: Int?,
        localNewsId: Int?,
        reportMissingId: Int?
    ) = flow {
        emit(
            apiService.getCommentsById(
                token,
                page,
                size,
                sort,
                status,
                freeNoticeBoardId,
                localNewsId,
                reportMissingId
            )
        )
    }

    suspend fun postComments(token: String, body: CommentModel) = flow {
        emit(apiService.postComments(token, body))
    }

    suspend fun editComments(token: String, body: CommentModel) = flow {
        emit(apiService.editComments(token, body))
    }
}