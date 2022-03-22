package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ItemMessageModel {
    @SerializedName("chatRoomId")
    @Expose
    var chatRoomId: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    var userId: String? = null // writer
    var name: String? = null
    var createdOn: String? = null
    var chatId: Int? = null
    var messageId: Long? = null
}