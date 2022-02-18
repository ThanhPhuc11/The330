package com.nagaja.the330.model

class PopularAreaModel(
    var id: Int? = null,
    var name: MutableList<NameModel>? = null,
    var priority: Int? = null,
    var status: String? = null,
    var city: CityModel? = null,
    var createdOn: String? = null
)