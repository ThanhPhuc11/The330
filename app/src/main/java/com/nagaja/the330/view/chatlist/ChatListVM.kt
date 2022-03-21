package com.nagaja.the330.view.chatlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ChatDetailModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChatListVM(
    private val repo: ChatListRepo
) : BaseViewModel() {
    val stateListRoom = mutableStateListOf<ChatDetailModel>()

    fun getChatList(
        token: String,
        page: Int,
        type: String? = null,
        startTime: String? = null,
        endTime: String? = null
    ) {
        viewModelScope.launch {
            repo.getChatList(token, page, size = 20, type, startTime, endTime)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListRoom.clear()
                    }
                    it.content?.let { data ->
                        stateListRoom.addAll(data)
                    }
                }
        }
    }
}