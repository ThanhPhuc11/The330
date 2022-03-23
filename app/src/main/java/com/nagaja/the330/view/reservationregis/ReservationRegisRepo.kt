package com.nagaja.the330.view.reservationregis

import com.nagaja.the330.model.ReservationModel
import com.nagaja.the330.network.ApiService
import kotlinx.coroutines.flow.flow

class ReservationRegisRepo(private val apiService: ApiService) {
    suspend fun getCompanyDetail(token: String, id: Int) = flow {
        emit(apiService.getCompanyDetail(token, id))
    }

    suspend fun makeReservation(token: String, body: ReservationModel) = flow {
        emit(apiService.makeReservation(token, body))
    }

    suspend fun getReservationAvailableTime(
        token: String,
        companyOwnerId: Int,
        dateBegin: String,
        dateEnd: String
    ) = flow {
        emit(apiService.getReservationAvailableTime(token, companyOwnerId, dateBegin, dateEnd))
    }
}