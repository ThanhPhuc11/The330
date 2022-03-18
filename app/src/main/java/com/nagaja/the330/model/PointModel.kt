package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PointModel {
    @SerializedName("amount")
    @Expose
    var amount: Int? = null

    @SerializedName("companyAdmin")
    @Expose
    var companyAdmin: CompanyAdminModel? = null

    @SerializedName("companyRequest")
    @Expose
    var companyRequest: CompanyModel? = null

    @SerializedName("consultingUser")
    @Expose
    var consultingUser: UserDetail? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("pointHistoryId")
    @Expose
    var pointHistoryId: Int? = null

    @SerializedName("pointProvidedType")
    @Expose
    var pointProvidedType: String? = null

    @SerializedName("pointUsageType")
    @Expose
    var pointUsageType: String? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null
}