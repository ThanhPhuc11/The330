package com.nagaja.the330.view.localnews

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.LocalNewsModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LocalNewsVM(
    private val repo: LocalNewsRepo
) : BaseViewModel() {
    var sort = "LASTEST"
    var listData = mutableListOf<LocalNewsModel>()
    var stateListData = mutableStateListOf<LocalNewsModel>()


    fun getListLocalNews(
        token: String,
        page: Int,
    ) {
        viewModelScope.launch {
            repo.getListLocalNews(
                token = token,
                page = page,
                size = 20,
                sort = sort,
                null
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListData.clear()
                    }
                    it.content?.let { it1 ->
                        stateListData.addAll(it1)
                    }
                }
        }
    }
}