package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SecondHandModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("seller")
    @Expose
    var seller: UserDetail? = null

    @SerializedName("transactionStatus")
    @Expose
    var transactionStatus: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("city")
    @Expose
    var city: CityModel? = null

    @SerializedName("district")
    @Expose
    var district: DistrictModel? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("peso")
    @Expose
    var peso: Double? = null

    @SerializedName("dollar")
    @Expose
    var dollar: Double? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null
}