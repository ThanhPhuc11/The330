package com.nagaja.the330.view.reservationregis

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.ReservationModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReservationRegisVM(
    private val repo: ReservationRegisRepo
) : BaseViewModel() {
    val stateEdtBooker = mutableStateOf(TextFieldValue(""))
    val stateEdtRequestNote = mutableStateOf(TextFieldValue(""))
    val stateEdtNumber = mutableStateOf(TextFieldValue(""))
    var date = ""
    var time = -1

    fun makeReservation(token: String) {
        val body = ReservationModel().apply {
            bookerName = stateEdtBooker.value.text
            bookerPhone = "0332718728"
            companyOwner = CompanyModel().apply {
                id = 4
            }
            requestNote = stateEdtRequestNote.value.text
            reservationNumber = stateEdtNumber.value.text.toInt()
            reservationDate = date
            reservationTime = time
        }
        viewModelScope.launch {
            repo.makeReservation(token, body)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {

                }
        }
    }
}