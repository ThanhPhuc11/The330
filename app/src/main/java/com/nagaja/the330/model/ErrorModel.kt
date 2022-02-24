package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ErrorModel {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("timestamp")
    @Expose
    var timestamp: String? = null

    @SerializedName("errorCode")
    @Expose
    var errorCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("subErrors")
    @Expose
    var subErrors: String? = null
}