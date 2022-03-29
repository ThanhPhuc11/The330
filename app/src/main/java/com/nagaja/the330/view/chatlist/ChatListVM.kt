package com.nagaja.the330.view.chatlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.RoomDetailModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChatListVM(
    private val repo: ChatListRepo
) : BaseViewModel() {
    var sort = ""
    val stateListRoom = mutableStateListOf<RoomDetailModel>()
    val total = mutableStateOf(0)
    fun getChatList(
        token: String,
        page: Int
    ) {
        viewModelScope.launch {
            repo.getChatList(token, page, size = 20, null, typeSearchChat = sort)
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
                        total.value = it.totalElements ?: 0
                    }
                    it.content?.let { data ->
                        stateListRoom.addAll(data)
                    }
                }
        }
    }
}