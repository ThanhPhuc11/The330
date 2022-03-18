package com.nagaja.the330.data

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nagaja.the330.R
import com.nagaja.the330.model.CompanyUsageModel
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.NotificationModel
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.utils.AppConstants

object GetDummyData {
    fun getCoutryAdrressSignup(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("KOREA", "대한민국"))
            add(KeyValueModel("PHILIPPINES", "필리핀"))
        }
    }

    fun getCountryNumber(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("82", "+82"))
            add(KeyValueModel("63", "+63"))
        }
    }

    fun getMoneyType(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("peso", "₱"))
            add(KeyValueModel("dollar", "$"))
        }
    }

    fun getSortFavoriteCompany(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("CREATED_ON", context.getString(R.string.sort_create_on)))
            add(KeyValueModel("FREQUENCY", context.getString(R.string.sort_frequency)))
        }
    }

    fun getTime1Hour(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("0", "00:00"))
            add(KeyValueModel("1", "01:00"))
            add(KeyValueModel("2", "02:00"))
            add(KeyValueModel("3", "03:00"))
            add(KeyValueModel("4", "04:00"))
            add(KeyValueModel("5", "05:00"))
            add(KeyValueModel("6", "06:00"))
            add(KeyValueModel("7", "07:00"))
            add(KeyValueModel("8", "08:00"))
            add(KeyValueModel("9", "09:00"))
            add(KeyValueModel("10", "10:00"))
            add(KeyValueModel("11", "11:00"))
            add(KeyValueModel("12", "12:00"))
            add(KeyValueModel("13", "13:00"))
            add(KeyValueModel("14", "14:00"))
            add(KeyValueModel("15", "15:00"))
            add(KeyValueModel("16", "16:00"))
            add(KeyValueModel("17", "17:00"))
            add(KeyValueModel("18", "18:00"))
            add(KeyValueModel("19", "19:00"))
            add(KeyValueModel("20", "20:00"))
            add(KeyValueModel("21", "21:00"))
            add(KeyValueModel("22", "22:00"))
            add(KeyValueModel("23", "23:00"))
        }
    }

    fun getSecondHandCategory(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel(AppConstants.SecondhandCategory.DIGITAL_DEVICE, "디지털기기"))
            add(KeyValueModel(AppConstants.SecondhandCategory.HOME_APPLIANCES, "생활가전"))
            add(KeyValueModel(AppConstants.SecondhandCategory.FURNITURE_INTERIOR, "가구/인테리어"))
            add(KeyValueModel(AppConstants.SecondhandCategory.BABY_PRODUCT, "유아용품"))
            add(KeyValueModel(AppConstants.SecondhandCategory.LIVING_PROCESSED_FOOD, "생활/가공식품"))
            add(KeyValueModel(AppConstants.SecondhandCategory.SPORT_LEISURE, "스포츠/레저"))
            add(KeyValueModel(AppConstants.SecondhandCategory.WOMEN_MISCELLANEOUS_GOOD, "여성잡화"))
            add(KeyValueModel(AppConstants.SecondhandCategory.WOMEN_CLOTHING, "여성의류"))
            add(KeyValueModel(AppConstants.SecondhandCategory.MEN_FASHION, "남성패션"))
            add(KeyValueModel(AppConstants.SecondhandCategory.MEN_ACCESSORIES, "남성잡화"))
            add(KeyValueModel(AppConstants.SecondhandCategory.GAME_HOBBIES, "게임/취미"))
            add(KeyValueModel(AppConstants.SecondhandCategory.BEAUTY, "뷰티/미용"))
            add(KeyValueModel(AppConstants.SecondhandCategory.PET_PRODUCT, "반려동물용품"))
            add(KeyValueModel(AppConstants.SecondhandCategory.BOOK_TICKET_ALBUM, "도서/티켓/음반"))
            add(KeyValueModel(AppConstants.SecondhandCategory.PLANT, "식물"))
            add(KeyValueModel(AppConstants.SecondhandCategory.OTHER, "기타중고물품"))
            add(KeyValueModel(AppConstants.SecondhandCategory.BUY, "삽니다"))
        }
    }

    fun getTitleSearch(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel(AppConstants.SearchTitle.COMPANY, "업체"))
            add(KeyValueModel(AppConstants.SearchTitle.FREE_NOTICE_BOARD, "자유게시판"))
            add(KeyValueModel(AppConstants.SearchTitle.RECRUIT_JOBSEARCH, "구인구직"))
            add(KeyValueModel(AppConstants.SearchTitle.SECONDHAND_MARKET, "중고마켓"))
            add(KeyValueModel(AppConstants.SearchTitle.REPORT_MISSING, "신고/실종자"))
        }
    }

    fun getSortSecondHandMarket(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("LASTEST", context.getString(R.string.lastest_order)))
            add(KeyValueModel("NEAREST", context.getString(R.string.close_net)))
            add(KeyValueModel("PRICE_DESC", context.getString(R.string.low_price_net)))
            add(KeyValueModel("PRICE_ASC", context.getString(R.string.high_price)))
        }
    }

    fun getSortMySecondHand(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("LASTEST", context.getString(R.string.lastest_order)))
            add(KeyValueModel("VIEW_COUNT", context.getString(R.string.view_order)))
            add(KeyValueModel("IN_TRANSACTION", context.getString(R.string.in_transition)))
            add(KeyValueModel("TRANSACTION_COMPLETE", context.getString(R.string.transaction_complete)))
        }
    }

    fun getSortLocalNews(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("LASTEST", context.getString(R.string.lastest_order)))
            add(KeyValueModel("VIEW_COUNT", context.getString(R.string.view_order)))
            add(KeyValueModel("COMMENT_COUNT", context.getString(R.string.comment_order)))
        }
    }

    fun getSortRecruitmentJobs(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("LASTEST", context.getString(R.string.lastest_order)))
            add(KeyValueModel("VIEW_COUNT", context.getString(R.string.view_order)))
        }
    }

    fun getSortReservation(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("ONE_WEEK", context.getString(R.string.one_week)))
            add(KeyValueModel("ONE_MONTH", context.getString(R.string.one_month)))
            add(KeyValueModel("THREE_MONTH", context.getString(R.string.three_month)))
            add(KeyValueModel("SIX_MONTH", context.getString(R.string.six_month)))
            add(KeyValueModel("ONE_YEAR", context.getString(R.string.one_year)))
        }
    }

    fun getSortReservationRoleCompany(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("TODAY", context.getString(R.string.one_week)))
            add(KeyValueModel("THREE_DAY", context.getString(R.string.one_week)))
            add(KeyValueModel("ONE_WEEK", context.getString(R.string.one_week)))
            add(KeyValueModel("LAST_ONE_MONTH", context.getString(R.string.one_month)))
            add(KeyValueModel("THREE_MONTH", context.getString(R.string.three_month)))
            add(KeyValueModel("SIX_MONTH", context.getString(R.string.six_month)))
            add(KeyValueModel("ONE_YEAR", context.getString(R.string.one_year)))
        }
    }

    fun getSortCompany(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("NAGAJA_RECOMMEND_ORDER", context.getString(R.string.nagaja_recommend_order)))
            add(KeyValueModel("DISTANCE_ORDER", context.getString(R.string.distance_order)))
            add(KeyValueModel("REGULAR_ORDER", context.getString(R.string.regular_order)))
        }
    }

    fun getFilterCompany(context: Context): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("ALL", context.getString(R.string.nagaja_recommend_order)))
            add(KeyValueModel("DELIVERY", context.getString(R.string.delivery_available)))
            add(KeyValueModel("RESERVATION", context.getString(R.string.reservation_available)))
            add(KeyValueModel("PICKUP_DROP", context.getString(R.string.pickup_dropup_available)))
            add(KeyValueModel("NAGAJA_AUTHEN", context.getString(R.string.nagaja_authentication)))
        }
    }

    fun getNotificationDetail(): MutableState<NotificationModel> {
        return mutableStateOf(NotificationModel().apply {
            id = 7
            question =  "Dummy 군인은 현역을 "
            answer = "<p>{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }{공지사항 게시글 전체 내용이 출력되는 칸입니다. }</p>".repeat(10)
            status = "ACTIVATED"
            priority = 7
            viewCount = 10
            createdOn ="2021. 10. 18"
        })
    }

    fun getNotificationList(): SnapshotStateList<NotificationModel> {
        val notices = mutableStateListOf<NotificationModel>()
        val list = mutableListOf<NotificationModel>()
        for ( i in 0..100) {
            list.add(NotificationModel().apply {
                id = i
                question = "Dummy 군인은 현역을 $i"
                answer = "<p>군인은 현역을 면한 후가 아니면 국무총리로 임명될 수 없다. 대통령은 즉시 이를 공포하여야 한다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다. 국회의원의 수는 법률로 정하되.</p>"
                status = "ACTIVATED"
                priority = 7
                viewCount = 100
                createdOn = "2022.03.16"
            })
        }
        notices.addAll(list)
        return  notices
    }

    fun getCompanyUsageList() : MutableList<CompanyUsageModel> {
        val list = mutableListOf<CompanyUsageModel>()
        for (i in 0..100) {
            list.add(CompanyUsageModel().apply {
                id = i
                name = "Dummy $i"
                numberOfUsers = 10
                date = "2022/03/18 00:00"
            })
        }
        return list
    }

    fun getRecruitmentList() : MutableList<RecruitmentJobsModel> {
        val list = mutableListOf<RecruitmentJobsModel>()
        for (i in 0..100) {
            list.add(RecruitmentJobsModel().apply {
                id = i
                title = "Dummy $i"
                body = "설명내용 일부 (등록된 글의 앞부분만 2줄까지 표기, 이후 ...) 설명내용 일부 (등록된 글의 앞부분만 2줄까지 표기, 이후 ...)"
                createdOn = "2022/03/18 00:00"
            })
        }
        return list
    }
}