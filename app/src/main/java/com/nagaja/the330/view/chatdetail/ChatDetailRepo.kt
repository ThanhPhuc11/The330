package com.nagaja.the330.view.chatdetail

import com.nagaja.the330.model.ItemMessageModel
import com.nagaja.the330.model.StartChatRequest
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ChatDetailRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }

    suspend fun startChat(token: String, body: StartChatRequest) = flow {
        emit(apiService.startChat(token, body))
    }

    suspend fun startChatByRoomId(token: String, roomId: Int) = flow {
        emit(apiService.startChatByRoomId(token, roomId))
    }

    suspend fun getChatDetail(token: String, roomId: Int, chatMesId: Int?) = flow {
        emit(apiService.getChatDetail(token, chatRoomId = roomId, chatMesId = chatMesId))
    }

    suspend fun sendMess(token: String, body: ItemMessageModel) = flow {
        emit(apiService.sendMess(token, body))
    }
}