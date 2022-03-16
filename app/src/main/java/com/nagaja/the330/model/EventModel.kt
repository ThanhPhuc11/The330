package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventModel {
    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("priority")
    @Expose
    var priority: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null
}