package com.nagaja.the330.view.freenoticeboard

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.FreeNoticeModel
import com.nagaja.the330.model.LocalNewsModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FreeNoticeVM(
    private val repo: FreeNoticeRepo
) : BaseViewModel() {
    var listData = mutableListOf<FreeNoticeModel>()
    var stateListData = mutableStateListOf<FreeNoticeModel>()


    fun getFreeNoticeBoard(
        token: String,
//        page: Int,
//        size: Int,
        sort: String = "LASTEST"
    ) {
        viewModelScope.launch {
            repo.getFreeNoticeBoard(
                token = token,
                page = 0,
                size = 10,
                sort = sort,
                null
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    listData.clear()
                    it.content?.let { it1 ->
                        listData.addAll(it1)
                        stateListData.clear()
                        stateListData.addAll(listData)
                    }
                }
        }
    }
}