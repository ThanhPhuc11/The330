package com.nagaja.the330.view.reservation

import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ReservationRepo(private val apiService: ApiService) {
    suspend fun getReservationMain(
        token: String,
        page: Int,
        size: Int,
        asCompany: Boolean,
        timeLimit: String,
        status: String?
    ) = flow {
        emit(apiService.getReservationMain(token, page, size, asCompany, timeLimit, status))
    }

    suspend fun reservationOverview(
        token: String,
        userId: Int,
        role: String
    ) = flow {
        emit(apiService.reservationOverview(token, userId = userId, role = role))
    }
}