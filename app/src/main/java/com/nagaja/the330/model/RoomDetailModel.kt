package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class RoomDetailModel {
    @SerializedName("actor")
    @Expose
    var actor: UserDetail? = null

    @SerializedName("actorRead")
    @Expose
    var actorRead: String? = null

    @SerializedName("companyOwner")
    @Expose
    var companyOwner: CompanyModel? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("recruitmentJob")
    @Expose
    var recruitmentJob: RecruitmentJobsModel? = null

    @SerializedName("secondHand")
    @Expose
    var secondHand: SecondHandModel? = null

    @SerializedName("target")
    @Expose
    var target: UserDetail? = null

    @SerializedName("targetRead")
    @Expose
    var targetRead: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("lastMessage")
    @Expose
    var lastMessage: ItemMessageModel? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}