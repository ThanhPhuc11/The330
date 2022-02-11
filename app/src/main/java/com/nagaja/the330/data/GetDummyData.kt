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
}