package com.nagaja.the330.view.reservationregis

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.ReservationModel
import com.nagaja.the330.model.UserDetail
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
                .onStart { callbackStart.value = Unit}
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackMakeReservationSuccess.value = true
                }
        }
    }
}