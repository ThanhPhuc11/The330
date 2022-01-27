package com.nagaja.the330.data

import com.nagaja.the330.model.KeyValueModel

object GetDummyData {
    fun getCoutryAdrressSignup(): MutableList<KeyValueModel> {
        return mutableListOf<KeyValueModel>().apply {
            add(KeyValueModel("KOREA", "대한민국"))
            add(KeyValueModel("PHILIPPINE", "필리핀"))
        }
    }
}