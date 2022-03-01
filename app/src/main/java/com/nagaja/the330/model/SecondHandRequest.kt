package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SecondHandRequest {
    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("city")
    @Expose
    var city: CityModel? = null

    @SerializedName("district")
    @Expose
    var district: DistrictModel? = null

    @SerializedName("dollar")
    @Expose
    var dollar: Double? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("peso")
    @Expose
    var peso: Double? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null
}