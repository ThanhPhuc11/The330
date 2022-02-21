package com.nagaja.the330.view.findId

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.PhoneAvailableModel
import com.nagaja.the330.model.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FindIdVM(private val repo: FindIdRepo) : BaseViewModel() {
    var _nationalNumber: String? = "82"
    var _otp: Int? = null
    var _snsType: String? = null

    val stateID = mutableStateOf("")

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

    val callbackFindIdSuccess = MutableLiveData<UserDetail>()

    fun findIdByPhone(nationalNum: String?, phoneNum: String?, otp: Int?) {
        viewModelScope.launch {
            repo.findIdByPhone(
                PhoneAvailableModel(
                    nationNumber = nationalNum,
                    phone = phoneNum,
                    otp = otp
                )
            )
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    stateID.value = it.name.toString()
                }
        }
    }
}