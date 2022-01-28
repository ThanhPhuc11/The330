package com.nagaja.the330.network

import com.nagaja.the330.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("auth/accessToken")
    suspend fun getAccessToken(@Query("userId") userId: Int): AuthTokenModel

    @POST("auth/kakao")
    suspend fun authWithKakao(@Body body: AuthRequest): AuthTokenModel

    @POST("auth/naver")
    suspend fun authWithNaver(@Body body: AuthRequest): AuthTokenModel

    @POST("auth/registration/userId")
    suspend fun authWithId(
        @Body body: UserDetail
    ): AuthTokenModel //201

    @POST("auth/userId")
    suspend fun loginWithId(
        @Body body: AuthRequest
    ): AuthTokenModel

    @POST("users/existByPhone")
    suspend fun checkExistPhone(
        @Body phone: PhoneAvailableModel
    ): PhoneAvailableModel

    @POST("users/phoneVerify")
    suspend fun sendPhone(
        @Body phone: PhoneAvailableModel
    ): PhoneAvailableModel

    @POST("users/phoneOtpValidate")
    suspend fun sendOTP(
        @Body phone: PhoneAvailableModel
    ): Response<PhoneAvailableModel>

    @GET("users")
    suspend fun getUserDetails(
        @Header("Authorization") token: String
    ): UserDetail

    @PATCH("users")
    suspend fun editUser(
        @Header("Authorization") token: String,
        @Body body: UserDetail
    ): Response<Unit> //204
}