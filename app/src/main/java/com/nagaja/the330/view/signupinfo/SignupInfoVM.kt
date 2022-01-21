package com.nagaja.the330.view.signupinfo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.utils.CommonUtils

class SignupInfoVM(private val repo: SignupInfoRepo) : BaseViewModel() {
    val stateErrorId: MutableState<String?> = mutableStateOf(null)
    val stateErrorPw: MutableState<String?> = mutableStateOf(null)
    val stateErrorRePw: MutableState<String?> = mutableStateOf(null)
    val pw = mutableStateOf(TextFieldValue(""))
    var confirmPw: String? = null


    //    이미 가입된 아이디입니다.
    fun checkInputId(it: String?) {
        if (it.isNullOrBlank()) {
            stateErrorId.value = "아이디를 입력해 주세요."
        } else if (it.length < 5) {
            stateErrorId.value = "아이디는 5~20자의 영문 소문자, 숫자 조합으로 입력해 주세요."
        } else {
            stateErrorId.value = ""
        }
    }

    fun checkInputPw(it: String) {
        if (it.isBlank()) {
            stateErrorPw.value = "비밀번호를 입력해 주세요."
        } else if (it.length < 8 || !CommonUtils.isPasswordValid(it)) {
            stateErrorPw.value = "비밀번호는 8~15자의 영어 대소문자, 숫자, 특수문자 조합으로 입력해주세요."
        } else {
            stateErrorPw.value = ""
        }
    }

    fun checkInputRePw() {
        if (confirmPw == null) return
        if (confirmPw != pw.value.text) {
            stateErrorRePw.value = "비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요."
        } else {
            stateErrorRePw.value = ""
        }
    }
}