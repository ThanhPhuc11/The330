package com.nagaja.the330.view.faq

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.FAQModel
import com.nagaja.the330.utils.AppDateUtils.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FAQViewModel(private val repo: FAQRepo)
    : BaseViewModel(){
    val fqaStateList = mutableStateListOf<FAQModel>()
    var cbFQADetail = mutableStateOf(FAQModel())

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
                    // utc to local
                    it.forEach { faq -> faq.apply {
                        createdOn = changeDateFormat(FORMAT_ISO, FORMAT_15, createdOn)
                    }}
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
                    cbFQADetail.value = it.apply {
                        createdOn = changeDateFormat(FORMAT_ISO, FORMAT_15, createdOn)
                    }
                }
        }
    }
}