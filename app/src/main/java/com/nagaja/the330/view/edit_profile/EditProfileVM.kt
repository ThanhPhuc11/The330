package com.nagaja.the330.view.edit_profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.UserDetail
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EditProfileVM(
    private val repo: EditProfileRepo
) : BaseViewModel() {
    val userDetailState: MutableState<UserDetail?> = mutableStateOf(null)

    val stateEdtName = mutableStateOf(userDetailState.value?.realName ?: "")
    val stateEdtNationNum = mutableStateOf(userDetailState.value?.nationNumber ?: "")
    val stateEdtPhone = mutableStateOf(userDetailState.value?.phone ?: "")
    val stateEdtNation = mutableStateOf(userDetailState.value?.nation ?: "")
    val stateEdtAddress = mutableStateOf(userDetailState.value?.address ?: "")
    val stateEdtDetailAddress = mutableStateOf(userDetailState.value?.detailAddress ?: "")

    var otp: Int? = null

    val cbNextScreen = MutableLiveData<Unit>()
    val cbUpdateSuccess = MutableLiveData<Unit>()


    fun getUserDetails(token: String) {
        viewModelScope.launch {
            repo.getUserDetails(token)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
//                    handleError(it)
//                    callbackUserFail.value = Unit
                }
                .collect {
                    callbackSuccess.value = Unit
                    userDetailState.value = it
                }
        }
    }

    fun checkChangePhone(token: String) {
        if (stateEdtPhone.value == userDetailState.value?.phone) {
            editUser(token)
        } else {
            cbNextScreen.value = Unit
        }
    }

    fun editUser(token: String) {
        val body = UserDetail().apply {
            realName = stateEdtName.value
            nation = stateEdtNation.value
            address = if (stateEdtNation.value == "KOREA") stateEdtAddress.value else ""
            detailAddress = stateEdtDetailAddress.value
            userEditType = "EDIT_MEMBER_INFO_WITHOUT_CHANGE_PHONE"
        }
        viewModelScope.launch {
            repo.editUser(token, body)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
//                    callbackUserFail.value = Unit
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackSuccess.value = Unit
                        cbUpdateSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }

    fun edtUserWithPhone(token: String) {
        val body = UserDetail().apply {
            realName = stateEdtName.value
            nationNumber = stateEdtNationNum.value
            phone = stateEdtPhone.value
            nation = stateEdtNation.value
            address = if (stateEdtNation.value == "KOREA") stateEdtAddress.value else ""
            detailAddress = stateEdtAddress.value
            otp = this@EditProfileVM.otp
            userEditType = "EDIT_MEMBER_INFO"
        }
        viewModelScope.launch {
            repo.editUser(token, body)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackSuccess.value = Unit
                        cbUpdateSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }
}