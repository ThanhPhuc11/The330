package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CompanyRecommendModel {
    @SerializedName("ads")
    @Expose
    var ads: String? = null

    @SerializedName("companyRequest")
    @Expose
    var companyRequest: CompanyModel? = null

    @SerializedName("exposeBegin")
    @Expose
    var exposeBegin: String? = null

    @SerializedName("exposeEnd")
    @Expose
    var exposeEnd: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("priority")
    @Expose
    var priority: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}