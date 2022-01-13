package com.nagaja.the330.model

data class AuthTokenModel(
    var accessToken: String? = null,
    var expiredOn: String,
    var fbToken: String,
    var refreshToken: String,
    var newUser: Boolean = true
)