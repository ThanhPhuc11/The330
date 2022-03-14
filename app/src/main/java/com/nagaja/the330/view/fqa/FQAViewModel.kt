package com.nagaja.the330.view.fqa

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.FQAModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FQAViewModel(private val repo: FQARepo)
    : BaseViewModel(){
    val fqaStateList = mutableStateListOf<FQAModel>()
    var cbFQADetail = mutableStateOf(FQAModel())

    fun getFQAs(token: String) {
        viewModelScope.launch {
            repo.getFQAs(token)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                }
                .collect {
                    callbackSuccess.value = Unit
                    fqaStateList.clear()
                    fqaStateList.addAll(it)
                }
        }
    }

    fun getFQADetail(token: String, id: Int) {
        viewModelScope.launch {
            repo.getFQADetail(token, id)
                .onStart { callbackStart.value = Unit }
                .catch {
                }
                .collect {
                    callbackSuccess.value = Unit
                    cbFQADetail.value = it
                }
        }
    }
}