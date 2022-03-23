package com.nagaja.the330.view.reservationcompany

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

class ReservationCompanyVM(
    private val repo: ReservationCompanyRepo
) : BaseViewModel() {
    var accessToken: String = ""
    var timeLimit: String = "ONE_DAY"
    var status: String? = null

    var listDataGeneral = mutableListOf<ReservationModel>()
    val stateListDataGeneral = mutableStateListOf<ReservationModel>()
    val stateTotalGeneralTab = mutableStateOf(0)

    var listDataCompany = mutableListOf<ReservationModel>()
    val stateListDataCompany = mutableStateListOf<ReservationModel>()
    val stateTotalCompanyTab = mutableStateOf(0)

    val total = mutableStateOf(0)
    val reservation_completed = mutableStateOf(0)
    val usage_completed = mutableStateOf(0)
    val reservation_cancel = mutableStateOf(0)

    fun reservationOverview(token: String, id: Int) {
        viewModelScope.launch {
            repo.reservationOverview(token, id, "company")
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    total.value = it.total ?: 0
                    reservation_completed.value = it.numCompleted ?: 0
                    usage_completed.value = it.numUsageCompleted ?: 0
                    reservation_cancel.value = it.numCancellation ?: 0
                }
        }
    }

    fun getReservationGeneral(token: String, page: Int) {
        viewModelScope.launch {
            repo.getReservationMain(
                token = token,
                page = page,
                size = 20,
                asCompany = false,
                timeLimit = timeLimit,
                status = status
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListDataGeneral.clear()
                        stateTotalGeneralTab.value = it.totalElements?:0
                    }
                    it.content?.let { data ->
                        listDataGeneral.clear()
                        listDataGeneral.addAll(data)
//                        stateListDataGeneral.clear()
                        stateListDataGeneral.addAll(listDataGeneral)
                    }
                }
        }
    }

    fun getReservationCompany(token: String, page: Int) {
        viewModelScope.launch {
            repo.getReservationMain(
                token = token,
                page = page,
                size = 20,
                asCompany = true,
                timeLimit = timeLimit,
                status = status
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListDataCompany.clear()
                        stateTotalCompanyTab.value = it.totalElements?:0
                    }
                    it.content?.let { data ->
                        listDataCompany.clear()
                        listDataCompany.addAll(data)
                        stateListDataCompany.addAll(listDataCompany)
                    }
                }
        }
    }

    fun editReservation(token: String, id: Int, enum: String) {
        viewModelScope.launch {
            repo.editReservation(token, mutableListOf(ReservationModel().apply {
                this.id = id
                status = enum
            }))
                .onStart {}
                .onCompletion {}
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        val index = stateListDataCompany.indexOfFirst { obj -> obj.id == id }
                        val newObj = stateListDataCompany[index].apply { status = enum }
                        updateItem(index, newObj, stateListDataCompany)
                    } else {
                        handleError2(it)
                    }
                }
        }
    }

    fun closeToday(token: String) {
        viewModelScope.launch {
            repo.closeToday(token)
                .onStart {}
                .onCompletion {}
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {

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