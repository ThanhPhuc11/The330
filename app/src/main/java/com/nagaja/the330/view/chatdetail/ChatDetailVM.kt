package com.nagaja.the330.view.chatdetail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ItemMessageModel
import com.nagaja.the330.model.RoomDetailModel
import com.nagaja.the330.model.StartChatRequest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChatDetailVM(
    private val repo: ChatDetailRepo
) : BaseViewModel() {
    val stateRoomInfo = mutableStateOf(RoomDetailModel())

    val stateListMess = mutableStateListOf<ItemMessageModel>()
    val listTemp = mutableListOf<ItemMessageModel>()

    val stateIsSeller = mutableStateOf(false)
    val stateBottomItem = mutableStateOf(ItemMessageModel())
    var isFirst = true
    var stateCapture = mutableStateOf(false)

    val callbackEndChatSuccess = MutableLiveData<Unit>()

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

    fun startChatByRoomId(token: String, roomId: Int) {
        viewModelScope.launch {
            repo.startChatByRoomId(token, roomId)
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

    fun getChatDetail(token: String, roomId: Int, chatMesId: Int? = null) {
        viewModelScope.launch {
            repo.getChatDetail(token, roomId, chatMesId, 10)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { data ->
                        data.onEachIndexed { index, itemMessageModel ->
                            itemMessageModel.isActor =
                                itemMessageModel.user?.id == stateRoomInfo.value.actor?.id
                        }
                        stateListMess.addAll(data.asReversed())
//                        if (isFirst) {
//                            isFirst = false
//                            if (data.isNotEmpty()) {
//                                stateBottomItem.value = data.last()
//                            }
//                        }
                    }
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

    fun endChat(token: String) {
        viewModelScope.launch {
            repo.endChat(token, stateRoomInfo.value.id!!)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackSuccess.value = Unit
                        callbackEndChatSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }
}