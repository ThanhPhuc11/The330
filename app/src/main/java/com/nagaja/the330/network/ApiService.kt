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
        @Query("group") group: String?
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

    @GET("secondhand_posts/in_my_page")
    suspend fun getMySecondHand(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String?,
        @Query("transactionStatus") transactionStatus: String?,
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
    suspend fun getLocalNewsDetail(
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

    @POST("free_notice_boards")
    suspend fun makePostNotice(
        @Header("Authorization") token: String,
        @Body body: FreeNoticePostRequest
    ): SecondHandPostResponse

    @POST("free_notice_boards/detail")
    suspend fun getFreeNotiDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
    ): FreeNoticeDetailModel

    @POST("free_notice_boards/report")
    suspend fun reportFreeNotice(
        @Header("Authorization") token: String,
        @Body body: ReportModel
    ): Response<Unit> //200

    @GET("report_missing/in_main")
    suspend fun getReportMissingList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("type") type: String,
        @Query("all") all: String?,
    ): ResponseModel<MutableList<ReportMissingModel>>

    @GET("report_missing/in_my_page")
    suspend fun getReportMissingMyPage(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("timeLimit") timeLimit: String,
        @Query("type") type: String?,
        @Query("status") status: String?,
        @Query("all") all: String?,
    ): ResponseModel<MutableList<ReportMissingModel>>

    @POST("report_missing/viewDetail")
    suspend fun getReportMissingDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
    ): ReportMissingModel

    @POST("report_missing")
    suspend fun makePostReportMissing(
        @Header("Authorization") token: String,
        @Body body: ReportMissingModel
    ): SecondHandPostResponse

    @GET("recruitment_jobs/in_main")
    suspend fun getRecruitmentList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("categoryType") type: String,
        @Query("all") all: String?,
    ): ResponseModel<MutableList<RecruitmentJobsModel>>

    @GET("recruitment_jobs/checkExistJobSearch")
    suspend fun checkExistJobSearch(
        @Header("Authorization") token: String,
    ): Boolean

    @POST("recruitment_jobs")
    suspend fun makePostRecruitJobs(
        @Header("Authorization") token: String,
        @Body body: RecruitmentJobsModel
    ): SecondHandPostResponse

    @POST("recruitment_jobs/viewDetail")
    suspend fun getRecruitJobsDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
    ): RecruitmentJobsModel

    @GET("reservations/{role}/{userId}/overview")
    suspend fun reservationOverview(
        @Header("Authorization") token: String,
        @Path("role") role: String,
        @Path("userId") userId: Int,
    ): ReservationOverviewModel

    @GET("reservations/in_main")
    suspend fun getReservationMain(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("asCompany") asCompany: Boolean,
        @Query("timeLimit") timeLimit: String,
        @Query("status") status: String?,
    ): ResponseModel<MutableList<ReservationModel>>

    @POST("reservations")
    suspend fun makeReservation(
        @Header("Authorization") token: String,
        @Body body: ReservationModel
    ): Response<Unit> //200

    @GET("company_requests/find")
    suspend fun findCompany(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("serviceTypes") serviceTypes: MutableList<String>?,
        @Query("cType") cType: String?,
        @Query("authentication") authentication: Boolean?,
        @Query("cityId") cityId: Int?,
        @Query("districtId") districtId: Int?,
        @Query("all") all: String?
    ): ResponseModel<MutableList<CompanyModel>>

    @GET("company_requests")
    suspend fun getCompanyDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): CompanyModel

    @GET("company_recommends")
    suspend fun getCompanyRecommendAds(
        @Header("Authorization") token: String
    ): ResponseModel<MutableList<CompanyRecommendModel>>

    @GET("faqs")
    suspend fun getFQAs(
        @Header("Authorization") token: String
    ): MutableList<FAQModel>

    @GET("faqs/detail")
    suspend fun getFQADetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): FAQModel
}