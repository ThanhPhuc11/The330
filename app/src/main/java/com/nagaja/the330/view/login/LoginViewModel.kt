package com.nagaja.the330.view.login

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.AuthRequest
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class LoginViewModel(
    private val repo: LoginRepository
) : BaseViewModel() {

    val callbackLoginKaKaoSuccess = MutableLiveData<AuthTokenModel>()
    val callbackLoginNaverSuccess = MutableLiveData<AuthTokenModel>()
    val callbackLoginGGSuccess = MutableLiveData<AuthTokenModel>()
    val callbackLoginFbSuccess = MutableLiveData<AuthTokenModel>()
    val callbackLoginIdSuccess = MutableLiveData<AuthTokenModel>()
    var snsType = ""
    var tokenTemp = ""
    var userTypeTemp = ""

    val edtId = mutableStateOf(TextFieldValue(""))
    val edtPw = mutableStateOf(TextFieldValue(""))
    val textError: MutableState<Int?> = mutableStateOf(null)
    val btnLogin = mutableStateOf(false)

    fun checkLogin() {
        if (edtId.value.text.isBlank() || edtPw.value.text.isBlank()) {
            textError.value = R.string.please_enter_both_id_pw
        } else {
            loginWithId()
        }
    }

    private fun loginWithId() {
        viewModelScope.launch {
            repo.loginWithId(AuthRequest(username = edtId.value.text, password = edtPw.value.text))
                .onStart { }
                .onCompletion { }
                .catch {
                    textError.value = R.string.id_pw_not_matches
                }
                .collect {
                    textError.value = null
                    callbackLoginIdSuccess.value = it
                }
        }
    }

    fun loginWithKakao(token: String, userType: String) {
        viewModelScope.launch(Dispatchers.Main) {
//            AuthRequest().apply {
//                token = "a"
//            }
            repo.loginWithKakao(AuthRequest(token, userType))
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
//                    handleError(it)
                    Log.e("handleError", it.message.toString())
                }
                .collect {
                    callbackLoginKaKaoSuccess.value = it
                    snsType = AppConstants.SNS_TYPE_KAKAO
                    tokenTemp = token
                    userTypeTemp = userType
                }
        }
    }

    fun loginWithNaver(token: String, userType: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.authWithNaver(AuthRequest(token, userType))
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
//                    handleError(it)
                    Log.e("handleError", it.message.toString())
                }
                .collect {
                    callbackLoginNaverSuccess.value = it
                    snsType = AppConstants.SNS_TYPE_NAVER
                    tokenTemp = token
                    userTypeTemp = userType
                    Log.e("token", it.accessToken.toString())
                }
        }
    }

    fun loginWithGoogle(token: String, userType: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.authWithGoogle(AuthRequest(token, userType))
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackLoginGGSuccess.value = it
                    snsType = AppConstants.SNS_TYPE_GOOGLE
                    tokenTemp = token
                    userTypeTemp = userType
                }
        }
    }

    fun loginWithFb(token: String, userType: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.authWithFb(AuthRequest(token, userType))
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackLoginFbSuccess.value = it
                    snsType = AppConstants.SNS_TYPE_FACEBOOK
                    tokenTemp = token
                    userTypeTemp = userType
                }
        }
    }

    fun loginGoogleAuth2(body: RequestBody, userType: String) {
        val url = "https://www.googleapis.com/oauth2/v4/token"
        viewModelScope.launch {
            repo.loginGoogleAuth2(url, body)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    showMessCallback.value = "Success"
                    loginWithGoogle(it.accessToken!!, userType)
                }
        }
    }

//    fun loginAgain() {
//        when (snsType) {
//            AppConstants.SNS_TYPE_KAKAO -> {
//                loginWithKakao(tokenTemp, userTypeTemp)
//            }
//            AppConstants.SNS_TYPE_NAVER -> {
//                loginWithNaver(tokenTemp, userTypeTemp)
//            }
//            AppConstants.SNS_TYPE_GOOGLE -> {
//                loginWithGoogle(tokenTemp, userTypeTemp)
//            }
//            AppConstants.SNS_TYPE_FACEBOOK -> {
//                loginWithFb(tokenTemp, userTypeTemp)
//            }
//        }
//    }
}