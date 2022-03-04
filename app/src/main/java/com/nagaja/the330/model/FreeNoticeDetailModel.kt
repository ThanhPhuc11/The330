package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class FreeNoticeDetailModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("notice")
    @Expose
    var notice: Boolean? = null

    @SerializedName("top")
    @Expose
    var top: Boolean? = null

    @SerializedName("user")
    @Expose
    var user: UserDetail? = null

    @SerializedName("companyName")
    @Expose
    var companyName: Any? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("contact")
    @Expose
    var contact: String? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null

    @SerializedName("commentCount")
    @Expose
    var commentCount: Int? = null

    @SerializedName("reportCount")
    @Expose
    var reportCount: Int? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null
}