package com.nagaja.the330.view.resetpassword

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.CommonUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ResetPwVM(private val repo: ResetPwRepo) : BaseViewModel() {
    var userDetail: UserDetail? = null
    val stateEdtID = mutableStateOf(TextFieldValue(""))
    val stateEdtPw = mutableStateOf(TextFieldValue(""))
    val stateEdtRePw = mutableStateOf(TextFieldValue(""))

    val stateIdNotFound = mutableStateOf(false)
    val statePwError = mutableStateOf("")
    val stateRePwError = mutableStateOf("")

    //handle string
    var disable = ""
    var notEntered = ""
    var notValidate = ""
    var notMatch = ""

    val callbackNextScreen = MutableLiveData<Unit>()
    val callbackChangeSuccess = MutableLiveData<Unit>()
    val callbackChangeFail = MutableLiveData<Unit>()

    fun checkMatchPassword(name: String?, nationNum: String?, phone: String?, otp: Int?) {
        resetState()
        if (stateEdtPw.value.text.isEmpty()) {
            statePwError.value = notEntered
            return
        } else if (!CommonUtils.isPasswordValid(stateEdtPw.value.text)) {
            statePwError.value = notValidate
            return
        } else if (stateEdtPw.value.text != stateEdtRePw.value.text) {
            stateRePwError.value = notMatch
            return
        }
        changePassword(name, nationNum, phone, otp)
    }

    private fun resetState() {
        statePwError.value = disable
        stateRePwError.value = disable
    }

    fun checkExistByID() {
        if (stateEdtID.value.text.isBlank()) return
        viewModelScope.launch {
            repo.checkExistByID(UserDetail().apply {
                name = stateEdtID.value.text
            })
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (it.available == false) {
                        stateIdNotFound.value = false
                        userDetail = it
                        callbackNextScreen.value = Unit
                    } else {
                        stateIdNotFound.value = true
                    }
                }
        }
    }

    private fun changePassword(name: String?, nationNum: String?, phone: String?, otp: Int?) {
        viewModelScope.launch {
            repo.changePassword(UserDetail().apply {
                password = stateEdtPw.value.text
                this.name = name
                this.nationNumber = nationNum
                this.phone = phone
                this.otp = otp
            })
                .onStart { }
                .onCompletion { }
                .catch {
                    callbackChangeFail.value = Unit
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackChangeSuccess.value = Unit
                    } else {
                        callbackChangeFail.value = Unit
                    }
                }
        }
    }
}