package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReservationModel {
    @SerializedName("bookerName")
    @Expose
    var bookerName: String? = null

    @SerializedName("bookerPhone")
    @Expose
    var bookerPhone: String? = null

    @SerializedName("companyOwner")
    @Expose
    var companyOwner: CompanyModel? = null

    @SerializedName("requestNote")
    @Expose
    var requestNote: String? = null

    @SerializedName("reservationDate")
    @Expose
    var reservationDate: String? = null

    @SerializedName("reservationNumber")
    @Expose
    var reservationNumber: Int? = null

    @SerializedName("reservationTime")
    @Expose
    var reservationTime: Int? = null
}