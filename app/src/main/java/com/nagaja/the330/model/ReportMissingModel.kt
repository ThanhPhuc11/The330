package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportMissingModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("writer")
    @Expose
    var writer: UserDetail? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("impossibleReason")
    @Expose
    var impossibleReason: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null

    @SerializedName("commentCount")
    @Expose
    var commentCount: Int? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null
}