package com.nagaja.the330.view.localnewsdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.LocalNewsModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LocalNewsDetailVM(
    private val repo: LocalNewsDetailRepo
) : BaseViewModel() {
    var localNewsModel = mutableStateOf(LocalNewsModel())


    fun getLocalNewsDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getLocalNewsDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    localNewsModel.value = it
                }
        }
    }
}