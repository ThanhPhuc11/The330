package com.nagaja.the330.model

class DistrictModel(
    var id: Int? = null,
    var name: MutableList<NameModel>? = null,
    var city: CityModel? = null,
    var priority: Int? = null,
)