package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class StartChatRequest {
    @SerializedName("recruitmentId")
    @Expose
    var recruitmentId: Int? = null

    @SerializedName("secondHandPostId")
    @Expose
    var secondHandPostId: Int? = null

    @SerializedName("userId")
    @Expose
    var userId: Int? = null
}