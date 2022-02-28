package com.nagaja.the330.view.verify_otp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.PhoneAvailableModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VerifyOTPVM(private val repo: VerifyOTPRepo) : BaseViewModel() {
    var _nationalNumber: String? = "82"
    var _otp: Int? = null
    var _snsType: String? = null

    // Phone
    val stateEdtPhone = mutableStateOf(TextFieldValue(""))
    val stateEnableFocusPhone = mutableStateOf(true)
    val stateBtnSendPhone = mutableStateOf(false)
    val cbNumberCoundown = mutableStateOf("인증요청")

    // OTP
    val stateEdtOTP = mutableStateOf(TextFieldValue(""))
    val stateEnableFocusOTP = mutableStateOf(false)
    val stateBtnSendOTP = mutableStateOf(false)
    val cbActivePhoneButtonTimer = mutableStateOf(false)

    val validatePhone = mutableStateOf(false)

    val callbackVerifySuccess = MutableLiveData<Unit>()

    fun checkPhone() {
        stateBtnSendPhone.value = stateEdtPhone.value.text.length >= 10
    }

    fun checkOTP() {
        stateBtnSendOTP.value = stateEdtOTP.value.text.isNotBlank()
    }

    fun checkExistByPhone(isCheckOldPhone: Boolean) {
        val phone = stateEdtPhone.value.text
        viewModelScope.launch(Dispatchers.IO) {
            repo.checkExistPhone(PhoneAvailableModel(phone = phone, nationNumber = _nationalNumber))
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if ((isCheckOldPhone && !it.available) || (!isCheckOldPhone && it.available)) {
                        _snsType = it.snsInfo?.type
                        sendPhone()
                    }
                }
        }
    }

    private fun sendPhone() {
        val phone = stateEdtPhone.value.text
        viewModelScope.launch {
            repo.sendPhone(PhoneAvailableModel(phone = phone, nationNumber = _nationalNumber))
                .onStart {
                    stateEnableFocusPhone.value = false
                    stateBtnSendPhone.value = false
                }
                .onCompletion { }
                .catch {
                    stateBtnSendPhone.value = true
                    stateEnableFocusPhone.value = true
                }
                .collect {
                    cbActivePhoneButtonTimer.value = true
                    stateEnableFocusOTP.value = true
                }
        }
    }

    fun sendOTP() {
        val otp = stateEdtOTP.value.text
        val phone = stateEdtPhone.value.text
        viewModelScope.launch {
            repo.sendOTP(
                PhoneAvailableModel(
                    phone = phone,
                    otp = otp.toInt(),
                    nationNumber = _nationalNumber
                )
            )
                .onStart {
                    stateEnableFocusOTP.value = false
                    stateBtnSendOTP.value = false
                }
                .onCompletion { }
                .catch {

                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 202) {
                        cbActivePhoneButtonTimer.value = false
                        validatePhone.value = true
                        _otp = it.body()?.otp
                        callbackVerifySuccess.value = Unit
                    }
                }

        }
    }

    private fun findIdByPhone() {
        viewModelScope.launch {
            repo.findIdByPhone(
                PhoneAvailableModel(
                    nationNumber = _nationalNumber,
                    phone = stateEdtPhone.value.text,
                    otp = _otp
                )
            )
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
//                    callbackFindIdSuccess.value = it.apply {
//                        this.snsType = _snsType
//                    }
                }
        }
    }
}