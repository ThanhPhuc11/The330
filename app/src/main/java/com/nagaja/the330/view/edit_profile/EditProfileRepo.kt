package com.nagaja.the330.view.edit_profile

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class EditProfileRepo(private val apiService: ApiService) {
    suspend fun getUserDetails(token: String) = flow {
        emit(apiService.getUserDetails(token))
    }
}