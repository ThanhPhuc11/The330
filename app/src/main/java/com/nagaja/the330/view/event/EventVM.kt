package com.nagaja.the330.view.event

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.BannerCompanyModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EventVM(private val repo: EventRepo) : BaseViewModel() {
    val stateListEvent = mutableStateListOf<BannerCompanyModel>()

    fun getEvent(token: String, page: Int) {
        viewModelScope.launch {
            repo.getEvent(token, page, 20)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (page == 0) {
                        stateListEvent.clear()
                    }
                    it.content?.let { it1 -> stateListEvent.addAll(it1) }
                }
        }
    }
}