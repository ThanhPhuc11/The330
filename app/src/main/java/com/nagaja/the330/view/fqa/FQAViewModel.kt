package com.nagaja.the330.view.fqa

import androidx.compose.runtime.MutableState
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
    val cbFQAsList: MutableState<MutableList<FQAModel>?> = mutableStateOf(null)
    val cbFQADetail = MutableLiveData<FQAModel>()

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
                    cbFQAsList.value = it
                }
        }
    }
}