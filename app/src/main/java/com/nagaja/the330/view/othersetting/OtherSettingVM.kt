package com.nagaja.the330.view.othersetting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class OtherSettingVM(
    private val repo: OtherSettingRepo
) : BaseViewModel() {
    val callbackLogoutSuccess = MutableLiveData<Unit>()
    val callbackWithDrawSuccess = MutableLiveData<Unit>()

    fun logout(token: String) {
        viewModelScope.launch {
            repo.logout(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackSuccess.value = Unit
                        callbackLogoutSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }

    fun withdraw(token: String) {
        viewModelScope.launch {
            repo.withdraw(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        callbackSuccess.value = Unit
                        callbackWithDrawSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }
}