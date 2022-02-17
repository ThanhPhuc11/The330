package com.nagaja.the330.utils

import java.io.File

object NameUtils {
    fun setFileName(id: Int?, file: File): String {
        return "AOS_${id}_${System.currentTimeMillis()}.${file.extension}"
    }
}