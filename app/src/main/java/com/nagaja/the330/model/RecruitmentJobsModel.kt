package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecruitmentJobsModel {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("writer")
    @Expose
    var writer: UserDetail? = null

    @SerializedName("companyRequest")
    @Expose
    var companyRequest: CompanyModel? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("snsInfo")
    @Expose
    var snsInfo: String? = null

    @SerializedName("regionInfo")
    @Expose
    var regionInfo: String? = null

    @SerializedName("city")
    @Expose
    var city: CityModel? = null

    @SerializedName("district")
    @Expose
    var district: DistrictModel? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<FileModel>? = null

    @SerializedName("viewCount")
    @Expose
    var viewCount: Int? = null

    @SerializedName("createdOn")
    @Expose
    var createdOn: String? = null
}