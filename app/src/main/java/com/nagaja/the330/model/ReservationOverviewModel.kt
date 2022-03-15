package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReservationOverviewModel {
    @SerializedName("numCancellation")
    @Expose
    var numCancellation: Int? = null

    @SerializedName("numCompleted")
    @Expose
    var numCompleted: Int? = null

    @SerializedName("numDeleted")
    @Expose
    var numDeleted: Int? = null

    @SerializedName("numUsageCompleted")
    @Expose
    var numUsageCompleted: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null
}