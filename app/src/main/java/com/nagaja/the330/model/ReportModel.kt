package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReportModel {
    @SerializedName("post")
    @Expose
    var post: IdentityModel? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null
}