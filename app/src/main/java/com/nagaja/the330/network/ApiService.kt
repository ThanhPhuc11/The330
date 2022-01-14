package com.nagaja.the330.network

import com.nagaja.the330.model.AuthRequest
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.model.UserDetail
import retrofit2.http.*

interface ApiService {
    @GET("auth/accessToken")
    suspend fun getAccessToken(@Query("userId") userId: Int): AuthTokenModel

    @POST("auth/kakao")
    suspend fun authWithKakao(@Body body: AuthRequest): AuthTokenModel

    @POST("auth/naver")
    suspend fun authWithNaver(@Body body: AuthRequest): AuthTokenModel

    @GET("users")
    suspend fun getUserDetails(
        @Header("Authorization") token: String
    ): UserDetail
}