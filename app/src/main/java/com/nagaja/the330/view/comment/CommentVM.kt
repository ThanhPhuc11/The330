package com.nagaja.the330.view.comment

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CommentModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommentVM(
    private val repo: CommentRepo
) : BaseViewModel() {
    val stateListComment = mutableStateListOf<CommentModel>()
    val stateCommentCount = mutableStateOf(0)


    fun getCommentsById(
        token: String,
        page: Int,
        size: Int,
        status: String?,
        freeNoticeBoardId: Int?,
        localNewsId: Int?,
        reportMissingId: Int?
    ) {
        viewModelScope.launch {
            repo.getCommentsById(
                token,
                page,
                size,
                "DESC",
                status,
                freeNoticeBoardId,
                localNewsId,
                reportMissingId
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    stateCommentCount.value = it.totalElements ?: 0
                    it.content?.let { it1 ->
                        stateListComment.clear()
                        stateListComment.addAll(it1)
                    }
                }
        }
    }

    fun postComments(token: String, body: CommentModel) {
        viewModelScope.launch {
            repo.postComments(token, body)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    stateListComment.add(0, it)
                    stateCommentCount.value = stateCommentCount.value + 1
                }
        }
    }

    fun editComments(token: String, commentId: Int) {
        viewModelScope.launch {
            repo.editComments(token, CommentModel().apply {
                id = commentId
                status = "DELETED"
            })
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        callbackSuccess.value = Unit
                        val index = stateListComment.indexOfFirst { it2 -> it2.id == commentId }
                        stateListComment.removeAt(index)
                        stateCommentCount.value = stateCommentCount.value - 1
                    }
                }
        }
    }
}