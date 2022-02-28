package com.nagaja.the330.view.mypage

import android.util.Log
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

class MyPageScreenVM(
    private val repo: MyPageScreenRepo
) : BaseViewModel() {
    val userDetailState: MutableState<UserDetail?> = mutableStateOf(null)
    val cbUpdateUserDataStore = MutableLiveData<UserDetail>()
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
                    cbUpdateUserDataStore.value = it
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("MYPAGE", "onCleared")
    }
}