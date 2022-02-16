package com.nagaja.the330.data

import android.content.Context
import com.nagaja.the330.R
import com.nagaja.the330.model.KeyValueModel

object GetDummyData {
    fun getCoutryAdrressSignup(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("KOREA", "대한민국"))
            add(KeyValueModel("PHILIPPINE", "필리핀"))
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
}