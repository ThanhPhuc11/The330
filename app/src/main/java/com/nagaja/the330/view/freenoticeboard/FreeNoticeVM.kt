package com.nagaja.the330.view.freenoticeboard

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.FreeNoticeModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FreeNoticeVM(
    private val repo: FreeNoticeRepo
) : BaseViewModel() {
    var keyword: String? = null
    var listData = mutableListOf<FreeNoticeModel>()
    var stateListData = mutableStateListOf<FreeNoticeModel>()


    fun getFreeNoticeBoard(
        token: String,
        page: Int,
//        size: Int,
        sort: String = "LASTEST"
    ) {
        viewModelScope.launch {
            repo.getFreeNoticeBoard(
                token = token,
                page = page,
                size = 20,
                sort = sort,
                keyword
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