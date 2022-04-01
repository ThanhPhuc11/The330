package com.nagaja.the330.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class MoneyExchangeModel {
    @SerializedName("usd")
    @Expose
    var usd: Double? = null

    @SerializedName("krw")
    @Expose
    var krw: Double? = null

    @SerializedName("php")
    @Expose
    var php: Double? = null
}