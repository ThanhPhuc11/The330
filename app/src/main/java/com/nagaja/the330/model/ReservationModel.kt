package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ReservationModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("companyOwner")
    @Expose
    var companyOwner: CompanyModel? = null

    @SerializedName("booker")
    @Expose
    var booker: UserDetail? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("bookerName")
    @Expose
    var bookerName: String? = null

    @SerializedName("bookerPhone")
    @Expose
    var bookerPhone: String? = null

    @SerializedName("reservationNumber")
    @Expose
    var reservationNumber: Int? = null

    @SerializedName("reservationDate")
    @Expose
    var reservationDate: String? = null

    @SerializedName("reservationTime")
    @Expose
    var reservationTime: Int? = null

    @SerializedName("reservationDateTime")
    @Expose
    var reservationDateTime: String? = null

    @SerializedName("requestNote")
    @Expose
    var requestNote: String? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("autoCancel")
    @Expose
    var autoCancel: Boolean? = null
}