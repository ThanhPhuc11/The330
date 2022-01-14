package com.nagaja.the330.view.general

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.AuthRequest
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GeneralViewModel(
    private val repo: GeneralRepository
) : BaseViewModel() {
    val callbackUserDetails = MutableLiveData<UserDetail>()
    val callbackUserFail = MutableLiveData<Unit>()

    fun getUserDetails(token: String) {
        viewModelScope.launch {
            repo.getUserDetails(token)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
//                    handleError(it)
                    callbackUserFail.value = Unit
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackUserDetails.value = it
                }
        }
    }
}