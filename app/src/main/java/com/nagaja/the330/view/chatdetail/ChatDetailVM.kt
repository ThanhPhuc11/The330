package com.nagaja.the330.view.chatdetail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ChatDetailModel
import com.nagaja.the330.model.ItemMessageModel
import com.nagaja.the330.model.StartChatRequest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChatDetailVM(
    private val repo: ChatDetailRepo
) : BaseViewModel() {
    val stateRoomInfo = mutableStateOf(ChatDetailModel())

    val stateListMess = mutableStateListOf<ItemMessageModel>()

    fun getUserDetails(token: String) {
        viewModelScope.launch {
            repo.getUserDetails(token)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
//                    handleError(it)
//                    callbackUserFail.value = Unit
                }
                .collect {
                    callbackSuccess.value = Unit
//                    userDetailState.value = it
                }
        }
    }

    fun startChat(token: String, body: StartChatRequest) {
        viewModelScope.launch {
            repo.startChat(token, body)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    stateRoomInfo.value = it
                }
        }
    }

    fun sendMess(token: String, body: ItemMessageModel) {
        viewModelScope.launch {
            repo.sendMess(token, body)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
//                    userDetailState.value = it
                }
        }
    }
}