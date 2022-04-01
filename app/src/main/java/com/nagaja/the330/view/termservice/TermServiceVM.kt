package com.nagaja.the330.view.termservice

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyConfigInfo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class TermServiceVM(private val repo: TermServiceRepo) : BaseViewModel() {
    val stateDataConfig = mutableStateOf(CompanyConfigInfo())

    fun getConfigInfo(token: String, type: String) {
        viewModelScope.launch {
            repo.getConfigInfo(token, type)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    stateDataConfig.value = it
                }
        }
    }
}