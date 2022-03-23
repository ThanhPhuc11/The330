package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReservationRemainModel {
    @SerializedName("reservationDate")
    @Expose
    var reservationDate: String? = null

    @SerializedName("reservationNumberRemain")
    @Expose
    var reservationNumberRemain: Int? = null

    @SerializedName("reservationNumberTotal")
    @Expose
    var reservationNumberTotal: Int? = null

    @SerializedName("reservationTime")
    @Expose
    var reservationTime: Int? = null
}