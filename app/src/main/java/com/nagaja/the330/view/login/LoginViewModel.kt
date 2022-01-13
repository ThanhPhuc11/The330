package com.nagaja.the330.view.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

class LoginViewModel(
    private val loginSNSRepository: LoginRepository
) : BaseViewModel() {

    val callbackLoginKaKaoSuccess = MutableLiveData<AuthTokenModel>()
    val callbackLoginNaverSuccess = MutableLiveData<AuthTokenModel>()
    var snsType = ""
    var tokenTemp = ""
    var userTypeTemp = ""

    fun loginWithKakao(token: String, userType: String) {
        viewModelScope.launch(Dispatchers.Main) {
//            AuthRequest().apply {
//                token = "a"
//            }
            loginSNSRepository.loginWithKakao(AuthRequest(token, userType))
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
            loginSNSRepository.authWithNaver(AuthRequest(token, userType))
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
//                    snsType = AppConstants.SNS_TYPE_NAVER
                    tokenTemp = token
                    userTypeTemp = userType
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