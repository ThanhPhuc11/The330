package com.nagaja.the330.model

data class AuthRequest(
    var token: String? = null,
    var userType: String? = null,
    var username: String? = null,
    var password: String? = null
)