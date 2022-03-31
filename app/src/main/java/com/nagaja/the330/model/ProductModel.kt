package com.nagaja.the330.model

data class ProductModel (
    var description: MutableList<NameModel>? = null,
    var id: Int? = null,
    var name: List<NameModel>? = null,
    var dollar: Double? = null,
    var peso: Double? = null,
    var won: Double? = null,
    var images: MutableList<FileModel>? = null,
    var priority: Int? = null
)