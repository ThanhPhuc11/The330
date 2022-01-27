package com.nagaja.the330.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseModel<V> : Serializable {
    @SerializedName("content")
    @Expose
    var content: V? = null

    @SerializedName("hasNext")
    @Expose
    var hasNext: Boolean? = null

    @SerializedName("totalElements")
    @Expose
    var totalElements: Int? = null

//    @SerializedName("additionalInfo")
//    @Expose
//    var additionalInfo: AdditionalInfo? = null
}