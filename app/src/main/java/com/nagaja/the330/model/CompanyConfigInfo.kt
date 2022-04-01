package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CompanyConfigInfo {
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("ceo")
    @Expose
    var ceo: String? = null

    @SerializedName("companyName")
    @Expose
    var companyName: String? = null

    @SerializedName("companyRegisterNumber")
    @Expose
    var companyRegisterNumber: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mailOrderBusinessReport")
    @Expose
    var mailOrderBusinessReport: String? = null

    @SerializedName("mainPhone")
    @Expose
    var mainPhone: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
}