package com.nagaja.the330.model

data class AuthTokenModel(
    var accessToken: String? = null,
    var expiredOn: String? = null,
    var fbToken: String? = null,
    var refreshToken: String? = null,
    var userType: String? = null,
    var tempPass: Boolean? = null,
)