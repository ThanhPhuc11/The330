package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class StartChatRequest {
    @SerializedName("recruitmentId")
    @Expose
    var recruitmentId: String? = null

    @SerializedName("secondHandPostId")
    @Expose
    var secondHandPostId: String? = null

    @SerializedName("userId")
    @Expose
    var userId: Int? = null
}