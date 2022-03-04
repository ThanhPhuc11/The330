package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FreeNoticePostRequest {
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("companyName")
    @Expose
    var companyName: String? = null

    @SerializedName("contact")
    @Expose
    var contact: String? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("title")
    @Expose
    var title: String? = null
}