package com.nagaja.the330.view.reservationcompany

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ReservationModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReservationCompanyVM(
    private val repo: ReservationCompanyRepo
) : BaseViewModel() {
    var timeLimit: String = "ONE_WEEK"
    var status: String? = null

    var listData = mutableListOf<ReservationModel>()
    val stateListData = mutableStateListOf<ReservationModel>()

    fun getReservationMain(token: String) {
        viewModelScope.launch {
            repo.getReservationMain(
                token = token,
                page = 0,
                size = 10,
                asCompany = false,
                timeLimit = timeLimit,
                status = status
            )
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { data ->
                        listData.clear()
                        listData.addAll(data)
                        stateListData.clear()
                        stateListData.addAll(listData)
                    }
                }
        }
    }
}