package com.nagaja.the330.view.resetpassword

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ResetPwVM(private val repo: ResetPwRepo) : BaseViewModel() {
    val stateIDInput = mutableStateOf(TextFieldValue(""))
    val stateIdNotFound = mutableStateOf(false)
    val callbackNextScreen = MutableLiveData<Unit>()

    fun checkExistByID() {
        if (stateIDInput.value.text.isBlank()) return
        viewModelScope.launch {
            repo.checkExistByID(UserDetail().apply {
                name = stateIDInput.value.text
            })
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (it.available == false) {
                        stateIdNotFound.value = false
                        callbackNextScreen.value = Unit
                    } else {
                        stateIdNotFound.value = true
                    }
                }
        }
    }
}