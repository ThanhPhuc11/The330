package com.nagaja.the330.view.mypagecompany

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.UserDetail
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MyPageCompanyScreenVM(
    private val repo: MyPageCompanyScreenRepo
) : BaseViewModel() {
    val userDetailState: MutableState<UserDetail?> = mutableStateOf(null)
    var companyDetailState = mutableStateOf(CompanyModel())

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

                    it.companyRequest?.id?.let { it1 -> getCompanyDetail(token, it1) }
                }
        }
    }

    fun getCompanyDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getCompanyDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    companyDetailState.value = it
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("MYPAGE", "onCleared")
    }
}