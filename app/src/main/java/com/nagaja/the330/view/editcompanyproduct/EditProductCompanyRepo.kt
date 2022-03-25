package com.nagaja.the330.view.editcompanyproduct

import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody

class EditProductCompanyRepo(private val apiService: ApiService) {

    suspend fun getCategory(token: String, group: String) = flow {
        emit(apiService.getCategory(token, group))
    }

    suspend fun editCompany(token: String, body: CompanyModel) = flow {
        emit(apiService.editCompany(token, body))
    }

    suspend fun uploadImage(url: String, body: RequestBody) = flow {
        emit(apiService.uploadImage(url, body))
    }
}