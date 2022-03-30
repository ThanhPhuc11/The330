package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class TotalCaseOverviewModel {
    @SerializedName("pointRemain")
    @Expose
    var pointRemain: Int? = null

    @SerializedName("consultationCount")
    @Expose
    var consultationCount: Int? = null

    @SerializedName("usageCount")
    @Expose
    var usageCount: Int? = null

    @SerializedName("reservationCount")
    @Expose
    var reservationCount: Int? = null

    @SerializedName("likedCount")
    @Expose
    var likedCount: Int? = null
}