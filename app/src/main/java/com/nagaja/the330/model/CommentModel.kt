package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CommentModel {
    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("companyRequest")
    @Expose
    var companyRequest: CompanyModel? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("freeNoticeBoard")
    @Expose
    var freeNoticeBoard: IdentityModel? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("localNews")
    @Expose
    var localNews: IdentityModel? = null

    @SerializedName("reportMissing")
    @Expose
    var reportMissing: IdentityModel? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("writer")
    @Expose
    var writer: UserDetail? = null
}