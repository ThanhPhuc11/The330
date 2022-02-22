package com.nagaja.the330.network

import com.nagaja.the330.model.*
import okhttp3.RequestBody
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

    @POST("users/existByName")
    suspend fun checkExistByID(
        @Body body: UserDetail
    ): UserDetail

    @POST("users/existByPhone")
    suspend fun checkExistPhone(
        @Body phone: PhoneAvailableModel
    ): PhoneAvailableModel

    @POST("users/find_by_phone")
    suspend fun findIdByPhone(
        @Body phone: PhoneAvailableModel
    ): UserDetail

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

    @PATCH("users/password_reset_main")
    suspend fun changePassword(
        @Body body: UserDetail
    ): Response<Unit> //204

    @GET("main_categories")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Query("group") group: String
    ): ResponseModel<MutableList<CategoryModel>>

    @GET("follows/target")
    suspend fun getFavoriteCompany(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): ResponseModel<MutableList<CompanyFavoriteModel>>

    @POST("follows")
    suspend fun followCompany(
        @Header("Authorization") token: String,
        @Query("targetId") targetId: Int
    ): Response<Unit> //201

    @DELETE("follows")
    suspend fun unfollowCompany(
        @Header("Authorization") token: String,
        @Query("targetId") targetId: Int
    ): Response<Unit> //204

    @POST("company_requests")
    suspend fun makeCompany(
        @Header("Authorization") token: String,
        @Body body: CompanyModel
    ): AppyCompanyResponse

    @PUT
    suspend fun uploadImage(
        @Url fullUrl: String?,
        @Body file: RequestBody
    ): Response<Unit>
}