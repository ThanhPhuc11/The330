package com.nagaja.the330.view.reservationregis

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReservationRegisVM(
    private val repo: ReservationRegisRepo
) : BaseViewModel() {
    var companyDetail = mutableStateOf(CompanyModel())
    var userDetail = mutableStateOf(UserDetail())
    val listAvailable = mutableListOf<ReservationRemainModel>()

    val stateEdtBooker = mutableStateOf(TextFieldValue(""))
    val stateEdtPhoneNumber = mutableStateOf(TextFieldValue())
    val stateEdtRequestNote = mutableStateOf(TextFieldValue(""))
    val stateEdtNumber = mutableStateOf(TextFieldValue(""))
    var id = -1
    var date = ""
    var time = -1

    val callbackMakeReservationSuccess = mutableStateOf(false)

    fun getCompanyDetail(
        token: String,
        id: Int = this@ReservationRegisVM.id
    ) {
        viewModelScope.launch {
            repo.getCompanyDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    companyDetail.value = it
                }
        }
    }

    fun makeReservation(token: String) {
        val body = ReservationModel().apply {
            bookerName = stateEdtBooker.value.text
            bookerPhone = stateEdtPhoneNumber.value.text
            companyOwner = CompanyModel().apply {
                id = this@ReservationRegisVM.id
            }
            requestNote = stateEdtRequestNote.value.text
            reservationNumber = stateEdtNumber.value.text.toInt()
            reservationDate = date
            reservationTime = time
        }
        viewModelScope.launch {
            repo.makeReservation(token, body)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        callbackSuccess.value = Unit
                        callbackMakeReservationSuccess.value = true
                    } else {
                        handleError2(it)
                    }
                }
        }
    }

    fun checkTimeAvailable(token: String) {
        viewModelScope.launch {
            repo.getReservationAvailableTime(token, companyDetail.value.id!!, date, date)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    listAvailable.clear()
                    listAvailable.addAll(it)
                    checkAvailable()
                }
        }
    }

    fun checkAvailable() {
        listAvailable.onEach { obj ->
            stateReservationTime[obj.reservationTime!!] =
                stateReservationTime[obj.reservationTime!!].copy(status = 1)
            val remain =
                (obj.reservationNumberRemain ?: 0) - stateEdtNumber.value.text.ifBlank { "0" }
                    .toInt()
            if (remain < 0) {
                stateReservationTime[obj.reservationTime!!] =
                    stateReservationTime[obj.reservationTime!!].copy(status = 0)
            }
        }
    }


    val stateReservationTime = mutableStateListOf(
        TimeAvailable("00:00"), TimeAvailable("00:30"),
        TimeAvailable("01:00"), TimeAvailable("01:30"),
        TimeAvailable("02:00"), TimeAvailable("02:30"),
        TimeAvailable("03:00"), TimeAvailable("03:30"),
        TimeAvailable("04:00"), TimeAvailable("04:30"),
        TimeAvailable("05:00"), TimeAvailable("05:30"),
        TimeAvailable("06:00"), TimeAvailable("06:30"),
        TimeAvailable("07:00"), TimeAvailable("07:30"),
        TimeAvailable("08:00"), TimeAvailable("08:30"),
        TimeAvailable("09:00"), TimeAvailable("09:30"),
        TimeAvailable("10:00"), TimeAvailable("10:30"),
        TimeAvailable("11:00"), TimeAvailable("11:30"),
        TimeAvailable("12:00"), TimeAvailable("12:30"),
        TimeAvailable("13:00"), TimeAvailable("13:30"),
        TimeAvailable("14:00"), TimeAvailable("14:30"),
        TimeAvailable("15:00"), TimeAvailable("15:30"),
        TimeAvailable("16:00"), TimeAvailable("16:30"),
        TimeAvailable("17:00"), TimeAvailable("17:30"),
        TimeAvailable("18:00"), TimeAvailable("18:30"),
        TimeAvailable("19:00"), TimeAvailable("19:30"),
        TimeAvailable("20:00"), TimeAvailable("20:30"),
        TimeAvailable("21:00"), TimeAvailable("21:30"),
        TimeAvailable("22:00"), TimeAvailable("22:30"),
        TimeAvailable("23:00"), TimeAvailable("23:30"),
    )
}