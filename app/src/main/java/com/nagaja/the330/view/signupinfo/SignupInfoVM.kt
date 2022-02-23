package com.nagaja.the330.view.signupinfo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.model.PhoneAvailableModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.CommonUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SignupInfoVM(private val repo: SignupInfoRepo) : BaseViewModel() {
    var authen_request = ""
    var please_enter_id = ""
    var guide_input_id = ""
    var please_enter_password = ""
    var password_error_format = ""
    var password_do_not_match_please_check_again = ""
    val stateErrorId: MutableState<String?> = mutableStateOf(null)
    val stateErrorPw: MutableState<String?> = mutableStateOf(null)
    val stateErrorRePw: MutableState<String?> = mutableStateOf(null)

    var confirmPw: String? = null
    var nation: String? = "KOREA"
    var _otp: Int? = null
    var lat: Float = 0f
    var long: Float = 0f

    val stateEdtRealName = mutableStateOf(TextFieldValue(""))
    val stateEdtNameID = mutableStateOf(TextFieldValue(""))
    val stateEdtPw = mutableStateOf(TextFieldValue(""))

    // Phone
    val stateEdtPhone = mutableStateOf(TextFieldValue(""))
    val stateEnableFocusPhone = mutableStateOf(true)
    val stateBtnSendPhone = mutableStateOf(false)
    val cbNumberCoundown = mutableStateOf(authen_request)

    // OTP
    val stateEdtOTP = mutableStateOf(TextFieldValue(""))
    val stateEnableFocusOTP = mutableStateOf(false)
    val stateBtnSendOTP = mutableStateOf(false)
    val cbActivePhoneButtonTimer = mutableStateOf(false)

    //check validate input
    val validateId = mutableStateOf(false)
    val validatePass = mutableStateOf(false)
    val validatePhone = mutableStateOf(false)
    val validateAddress = mutableStateOf(false)
    val validateTermRequire = mutableStateOf(false)

    //address
    val stateNationNum = mutableStateOf("82")
    val stateEdtAddress = mutableStateOf(TextFieldValue(""))
    val stateEdtAddressDetail = mutableStateOf(TextFieldValue(""))

    val callbackRegisSuccess = MutableLiveData<AuthTokenModel>()
    val callbackUpdateSNSInfoSuccess = MutableLiveData<Unit>()


    //    이미 가입된 아이디입니다.
    fun checkInputId(it: String?) {
        if (it.isNullOrBlank()) {
            stateErrorId.value = please_enter_id
            validateId.value = false
        } else if (it.length < 5) {
//            stateErrorId.value = "아이디는 5~20자의 영문 소문자, 숫자 조합으로 입력해 주세요."
            stateErrorId.value = guide_input_id
            validateId.value = false
        } else {
            stateErrorId.value = ""
            validateId.value = true
        }
    }

    fun checkInputPw(it: String) {
        if (it.isBlank()) {
            stateErrorPw.value = please_enter_password
        } else if (it.length < 8 || !CommonUtils.isPasswordValid(it)) {
            stateErrorPw.value = password_error_format
        } else {
            stateErrorPw.value = ""
        }
        checkValidatePw()
    }

    fun checkInputRePw() {
        if (confirmPw == null) return
        if (confirmPw != stateEdtPw.value.text) {
            stateErrorRePw.value = password_do_not_match_please_check_again
        } else {
            stateErrorRePw.value = ""
        }
        checkValidatePw()
    }

    private fun checkValidatePw() {
        validatePass.value = stateErrorPw.value == "" && stateErrorRePw.value == ""
    }

    fun checkPhone() {
        stateBtnSendPhone.value = stateEdtPhone.value.text.length >= 10
    }

    fun checkOTP() {
        stateBtnSendOTP.value = stateEdtOTP.value.text.isNotBlank()
    }

    fun checkExistByPhone() {
        val phone = stateEdtPhone.value.text
        val nationNum = stateNationNum.value
        viewModelScope.launch {
            repo.checkExistPhone(PhoneAvailableModel(phone = phone, nationNumber = nationNum))
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (it.available) {
                        sendPhone()
                    } else {
                        stateEnableFocusPhone.value = true
                        cbActivePhoneButtonTimer.value = false
                        cbNumberCoundown.value = authen_request
                        showMessCallback.value = "phone number already in use!"
                    }
                }
        }
    }

    private fun sendPhone() {
        val phone = stateEdtPhone.value.text
        viewModelScope.launch {
            repo.sendPhone(PhoneAvailableModel(phone = phone, nationNumber = stateNationNum.value))
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
                    nationNumber = stateNationNum.value
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
                    }
                }

        }
    }

    fun authWithId() {
        viewModelScope.launch {
            repo.authWithId(UserDetail().apply {
                realName = stateEdtRealName.value.text
                name = stateEdtNameID.value.text
                password = stateEdtPw.value.text
                nationNumber = stateNationNum.value
                phone = stateEdtPhone.value.text
                nation = this@SignupInfoVM.nation
                address = stateEdtAddress.value.text + stateEdtAddressDetail.value.text
                latitude = lat
                longitude = long
                agreePolicy = true
                otp = _otp
            })
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    callbackRegisSuccess.value = it
                }
        }
    }

    fun updateInfoSNS(token: String) {
        viewModelScope.launch {
            repo.editUser(token, UserDetail().apply {
                realName = stateEdtRealName.value.text
                name = stateEdtNameID.value.text
                nationNumber = stateNationNum.value
                phone = stateEdtPhone.value.text
                nation = this@SignupInfoVM.nation
                address = stateEdtAddress.value.text + stateEdtAddressDetail.value.text
                latitude = lat
                longitude = long
                agreePolicy = true
                otp = _otp
                userEditType = "SIGN_UP_WITH_SNS"
            })
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackUpdateSNSInfoSuccess.value = Unit
                    }
                }
        }
    }
}