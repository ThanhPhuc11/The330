package com.nagaja.the330.data

import android.content.Context
import com.nagaja.the330.R
import com.nagaja.the330.model.KeyValueModel
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

    fun getSortSecondHandMarket(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("LASTEST", "최신순"))
            add(KeyValueModel("NEAREST", "가까운 순"))
            add(KeyValueModel("PRICE_DESC", "저가 순"))
            add(KeyValueModel("PRICE_ASC", "고가 순"))
        }
    }
}