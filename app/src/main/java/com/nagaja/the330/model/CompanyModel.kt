package com.nagaja.the330.model

class CompanyModel {
    var id: Int? = null
    var name: MutableList<NameAreaModel>? = null
    var images: List<Any>? = null
    var popularArea: PopularAreaModel? = null
    var city: CityModel? = null
    var district: DistrictModel? = null
    var address: String? = null
    var description: MutableList<NameAreaModel>? = null
    var chargeName: String? = null
    var chargePhone: String? = null
    var chargeEmail: String? = null
    var chargeFacebook: String? = null
    var chargeKakao: String? = null
    var chargeLine: String? = null
    var serviceTypes: List<String>? = null
    var openHour: Int? = null
    var closeHour: Int? = null
    var reservationTime: List<Int>? = null
    var reservationNumber: Int? = null
    var paymentMethod: String? = null
    var ctype: String? = null
    var file: String? = null
}