package com.nagaja.the330.view.secondhandmarket

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class SecondHandMarketRepo(private val apiService: ApiService) {
    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }

    suspend fun secondHandMarket(
        token: String,
        cityId: Int?,
        districtId: Int?,
        secondhandCategoryType: String?,
        page: Int,
        size: Int,
        sort: String,
        all: String?
    ) = flow {
        emit(
            apiService.secondHandMarket(
                token = token,
                cityId = cityId,
                districtId = districtId,
                secondhandCategoryType = secondhandCategoryType,
                page = page,
                size = size,
                sort = sort,
                all = all
            )
        )
    }
}