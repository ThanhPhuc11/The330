package com.nagaja.the330.model

data class TimeAvailable(var time: String, var status: Int = 0)
// 0 : Disable
// 1: selectable
// 2: selected