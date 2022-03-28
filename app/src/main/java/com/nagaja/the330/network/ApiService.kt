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

    @PUT("fcm")
    suspend fun registerFCM(
        @Header("Authorization") token: String,
        @Body tokenfcm: TokenFCMRequest
    ): Response<Unit>

    @GET("main_categories")
    suspend fun getCategory(
        @Header("Authorization") token: String,
        @Query("group") group: String?
    ): ResponseModel<MutableList<CategoryModel>>

    @GET("follows/actor")
    suspend fun getFollowMe(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("followType") followType: String?,
    ): ResponseModel<MutableList<CompanyFavoriteModel>>

    @GET("follows/target")
    suspend fun getMyFollow(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("followType") followType: String?
    ): ResponseModel<MutableList<CompanyFavoriteModel>>

    @POST("follows")
    suspend fun followCompany(
        @Header("Authorization") token: String,
        @Query("targetId") targetId: Int,
        @Query("followType") followType: String?
    ): Response<Unit> //201

    @DELETE("follows")
    suspend fun unfollowCompany(
        @Header("Authorization") token: String,
        @Query("targetId") targetId: Int,
        @Query("followType") followType: String?
    ): Response<Unit> //204

    @GET("follows/checkFollow")
    suspend fun checkFollowCompany(
        @Header("Authorization") token: String,
        @Query("targetId") targetId: Int,
        @Query("followType") followType: String?
    ): Boolean

    @POST("company_requests")
    suspend fun makeCompany(
        @Header("Authorization") token: String,
        @Body body: CompanyModel
    ): AppyCompanyResponse

    @PATCH("company_requests")
    suspend fun editCompany(
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

    @GET("areas")
    suspend fun getPopularAreas(
        @Header("Authorization") token: String,
    ): ResponseModel<MutableList<DistrictModel>>

    @POST("secondhand_posts")
    suspend fun makeSecondhandPost(
        @Header("Authorization") token: String,
        @Body body: SecondHandRequest
    ): SecondHandPostResponse

    @PATCH("secondhand_posts")
    suspend fun editSecondhandPost(
        @Header("Authorization") token: String,
        @Body body: MutableList<SecondHandModel>
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
        @Query("latitude") latitude: Float?,
        @Query("longitude") longitude: Float?,
        @Query("secondhandCategoryType") secondhandCategoryType: String?,
        @Query("all") all: String?,
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

    @GET("recruitment_jobs/in_my_page")
    suspend fun getRecruitmentMypage(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("timeLimit") timeLimit: String,
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

    @PATCH("recruitment_jobs")
    suspend fun editPostRecruitJobs(
        @Header("Authorization") token: String,
        @Body body: RecruitmentJobsModel
    ): SecondHandPostResponse

    @GET("recruitment_jobs/employmentConfirm/check")
    suspend fun checkConfirm(
        @Header("Authorization") token: String,
        @Query("recruitmentJobId") recruitmentJobId: Int
    ): Boolean

    @POST("recruitment_jobs/employmentConfirm")
    suspend fun confirmRecruit(
        @Header("Authorization") token: String,
        @Query("recruitmentJobId") recruitmentJobId: Int
    ): Response<Unit>

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

    @PATCH("reservations")
    suspend fun editReservation(
        @Header("Authorization") token: String,
        @Body body: List<ReservationModel>
    ): Response<Unit> //200

    @POST("reservations/closeToday")
    suspend fun closeToday(
        @Header("Authorization") token: String
    ): Response<Unit> //200

    @GET("reservations/closeToday/check")
    suspend fun checkCloseToday(
        @Header("Authorization") token: String,
        @Query("companyId") companyId: Int,
    ): Boolean

    @GET("reservations/getReservationAvailableTime")
    suspend fun getReservationAvailableTime(
        @Header("Authorization") token: String,
        @Query("companyOwnerId") companyOwnerId: Int,
        @Query("dateBegin") dateBegin: String,
        @Query("dateEnd") dateEnd: String,
    ): MutableList<ReservationRemainModel>

    @GET("company_requests/find")
    suspend fun findCompany(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("serviceTypes") serviceTypes: MutableList<String>?,
        @Query("cType") cType: String?,
        @Query("authentication") authentication: Boolean?,
        @Query("latitude") latitude: Float?,
        @Query("longitude") longitude: Float?,
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

    @GET("comments")
    suspend fun getCommentsById(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("status") status: String?,
        @Query("freeNoticeBoardId") freeNoticeBoardId: Int?,
        @Query("localNewsId") localNewsId: Int?,
        @Query("reportMissingId") reportMissingId: Int?,
    ): ResponseModel<MutableList<CommentModel>>

    @POST("comments")
    suspend fun postComments(
        @Header("Authorization") token: String,
        @Body body: CommentModel
    ): CommentModel

    @PATCH("comments")
    suspend fun editComments(
        @Header("Authorization") token: String,
        @Body body: CommentModel
    ): Response<Unit>

    @GET("points")
    suspend fun getPoints(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("pointTransactionType") pointTransactionType: String,
        @Query("timeLimit") timeLimit: String,
    ): ResponseModel<MutableList<PointModel>>

    @GET("chat/chatList")
    suspend fun getChatList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String?,
        @Query("typeSearchChat") typeSearchChat: String?,
    ): ResponseModel<MutableList<RoomDetailModel>>

    @POST("chat/start")
    suspend fun startChat(
        @Header("Authorization") token: String,
        @Body body: StartChatRequest
    ): RoomDetailModel

    @GET("chat/startByChatRoom/{chatRoomId}")
    suspend fun startChatByRoomId(
        @Header("Authorization") token: String,
        @Path("chatRoomId") chatRoomId: Int
    ): RoomDetailModel

    @GET("chat/chatList/findNearMes/{chatRoomId}")
    suspend fun getChatDetail(
        @Header("Authorization") token: String,
        @Path("chatRoomId") chatRoomId: Int,
        @Query("chatMesId") chatMesId: Int?,
        @Query("size") size: Int,
    ): ResponseModel<MutableList<ItemMessageModel>>

    @POST("chat/newMessage")
    suspend fun sendMess(
        @Header("Authorization") token: String,
        @Body body: ItemMessageModel
    ): Response<Unit>

    @GET("banners/top/getAllActive")
    suspend fun getBannerCompany(
        @Header("Authorization") token: String,
        @Query("cType") cType: String,
    ): ResponseModel<MutableList<BannerCompanyModel>>

    @GET("banners/event/getAllActive")
    suspend fun getBannerHome(
        @Header("Authorization") token: String
    ): ResponseModel<MutableList<BannerCompanyModel>>

    @GET("event_notices")
    suspend fun getEvent(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseModel<MutableList<BannerCompanyModel>>

    @GET("event_notices/detail")
    suspend fun getEventDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ResponseModel<MutableList<BannerCompanyModel>>

    @GET("config/info")
    suspend fun getConfigCompanyInfo(
        @Header("Authorization") token: String,
    ): CompanyConfigInfo
}