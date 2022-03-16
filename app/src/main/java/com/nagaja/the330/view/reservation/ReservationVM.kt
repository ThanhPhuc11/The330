package com.nagaja.the330.view.reservation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    var timeLimit: String = "ONE_WEEK"
    var status: String? = null

    var listData = mutableListOf<ReservationModel>()
    val stateListData = mutableStateListOf<ReservationModel>()

    val total = mutableStateOf(0)
    val reservation_completed = mutableStateOf(0)
    val usage_completed = mutableStateOf(0)
    val reservation_cancel = mutableStateOf(0)

    fun reservationOverview(token: String, id: Int) {
        viewModelScope.launch {
            repo.reservationOverview(token, id, "general")
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    total.value = it.total ?: 0
                    reservation_completed.value = it.numCompleted ?: 0
                    usage_completed.value = it.numUsageCompleted ?: 0
                    reservation_cancel.value = it.numCancellation ?: 0
                }
        }
    }

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

    fun cancelReservation(token: String, id: Int) {
        viewModelScope.launch {
            repo.editReservation(token, mutableListOf(ReservationModel().apply {
                this.id = id
                status = "RESERVATION_CANCELED"
            }))
                .onStart {}
                .onCompletion {}
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        stateListData.forEach { obj->
                            if (obj.id == id) {
                                stateListData.remove(obj)
                            }
                        }
                    } else {
                        handleError2(it)
                    }
                }
        }
    }

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}