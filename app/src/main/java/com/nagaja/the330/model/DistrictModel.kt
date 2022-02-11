package com.nagaja.the330.model

class DistrictModel(
    var id: Int? = null,
    var name: MutableList<NameAreaModel>? = null,
    var city: CityModel? = null,
    var priority: Int? = null,
)