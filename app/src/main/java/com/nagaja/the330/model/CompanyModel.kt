package com.nagaja.the330.model

class CompanyModel {
    var id: Int? = null
    var name: MutableList<NameModel>? = null
    var images: MutableList<FileModel>? = null
    var popularArea: PopularAreaModel? = null
    var city: CityModel? = null
    var district: DistrictModel? = null
    var address: String? = null
    var description: MutableList<NameModel>? = null
    var chargeName: String? = null
    var chargePhone: String? = null
    var chargeEmail: String? = null
    var chargeFacebook: String? = null
    var chargeKakao: String? = null
    var chargeLine: String? = null
    var serviceTypes: MutableList<String>? = null
    var openHour: Int? = null
    var closeHour: Int? = null
    var reservationTime: List<Int>? = null
    var reservationNumber: Int? = null
    var paymentMethod: String? = null
    var ctype: String? = null
    var companyAuthentication: Boolean? = null
    var file: String? = null
    var fileTemp: FileModel? = null
    var products: MutableList<ProductModel>? = null
    var likedCount: Int? = null
    var numberRecommend: Int? = null
    var pointCharge: Int? = null
    var pointRemain: Int? = null
    var pointUse: Int? = null
    var usageCount: Int? = null
    var user: UserDetail? = null

    //field for request body
    var popularAreaId: Int? = null
    var cityId: Int? = null
    var districtId: Int? = null
}