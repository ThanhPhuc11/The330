package com.nagaja.the330.view.reservation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ReservationModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReservationVM(
    private val repo: ReservationRepo
) : BaseViewModel() {
    var listData = mutableListOf<ReservationModel>()
    val stateListData = mutableStateListOf<ReservationModel>()

    fun getReservationMain(token: String) {
        viewModelScope.launch {
            repo.getReservationMain(
                token = token,
                page = 0,
                size = 10,
                asCompany = false,
                timeLimit = "ONE_YEAR",
                status = null
            )
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { data ->
                        listData.clear()
                        listData.addAll(data)
                        stateListData.addAll(listData)
                    }
                }
        }
    }
}