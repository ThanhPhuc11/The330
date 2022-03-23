package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BannerCompanyModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("bannerType")
    @Expose
    var bannerType: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("deepLink")
    @Expose
    var deepLink: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("priority")
    @Expose
    var priority: Int? = null

    @SerializedName("startTime")
    @Expose
    var startTime: String? = null

    @SerializedName("endTime")
    @Expose
    var endTime: String? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("ctype")
    @Expose
    var ctype: String? = null
}