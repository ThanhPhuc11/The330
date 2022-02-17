package com.nagaja.the330.utils

object NameUtils {
    fun setFileName(id: Int?): String {
        return "AOS_${id}_${System.currentTimeMillis()}"
    }
}