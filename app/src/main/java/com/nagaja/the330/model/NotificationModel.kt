package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationModel {
    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("priority")
    @Expose
    var priority: Int? = null

    @SerializedName("question")
    @Expose
    var question: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null

}