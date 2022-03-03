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

    @POST("auth/google")
    suspend fun authWithGoogle(@Body body: AuthRequest): AuthTokenModel

    @POST("auth/facebook")
    suspend fun authWithFb(@Body body: AuthRequest): AuthTokenModel

    @POST("auth/registration/userId")
    suspend fun authWithId(
        @Body body: UserDetail
    ): AuthTokenModel //201

    @POST
    suspend fun loginGoogleAuth2(
        @Url fullUrl: String?,
        @Body file: RequestBody
    ): GoogleResponse

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

    @GET("users/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit> // 204

    @DELETE("users")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<Unit> // 204

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


    @GET("areas/city")
    suspend fun getCity(
        @Header("Authorization") token: String,
    ): ResponseModel<MutableList<CityModel>>

    @GET("areas/district")
    suspend fun getDistrict(
        @Header("Authorization") token: String,
        @Query("cityId") cityId: Int
    ): ResponseModel<MutableList<DistrictModel>>

    @POST("secondhand_posts")
    suspend fun makeSecondhandPost(
        @Header("Authorization") token: String,
        @Body body: SecondHandRequest
    ): SecondHandPostResponse

    @POST("secondhand_posts/viewDetail")
    suspend fun viewDetailSecondhandPost(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): SecondHandModel

    @GET("secondhand_posts/in_main")
    suspend fun secondHandMarket(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("cityId") cityId: Int?,
        @Query("districtId") districtId: Int?,
        @Query("secondhandCategoryType") secondhandCategoryType: String?,
    ): ResponseModel<MutableList<SecondHandModel>>

    @GET("local_news/in_main")
    suspend fun getListLocalNews(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("all") all: String?,
    ): ResponseModel<MutableList<LocalNewsModel>>

    @POST("local_news/viewDetail")
    suspend fun getListLocalNewsDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
    ): LocalNewsModel

    @GET("free_notice_boards")
    suspend fun getFreeNoticeBoard(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("all") all: String?,
    ): ResponseModel<MutableList<FreeNoticeModel>>
}