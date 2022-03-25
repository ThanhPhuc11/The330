package com.nagaja.the330.view.editcompany

import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class EditCompanyRepo(private val apiService: ApiService) {
    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }

    suspend fun getCategory(token: String, group: String) = flow {
        emit(apiService.getCategory(token, group))
    }

    suspend fun editCompany(token: String, body: CompanyModel) = flow {
        emit(apiService.editCompany(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }

    suspend fun getCity(token: String) = flow {
        emit(apiService.getCity(token))
    }

    suspend fun getDistrict(token: String, cityId: Int) = flow {
        emit(apiService.getDistrict(token, cityId))
    }

    suspend fun getPopularAreas(token: String) = flow {
        emit(apiService.getPopularAreas(token))
    }
}