package com.nagaja.the330.view.freenoticedetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.FreeNoticeDetailModel
import com.nagaja.the330.model.ReportModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FreeNoticeDetailVM(
    private val repo: FreeNoticeDetailRepo
) : BaseViewModel() {
    var freeNoticeDetailModel = mutableStateOf(FreeNoticeDetailModel())
    val callbackReportSuccess = MutableLiveData<Unit>()

    fun getFreeNotiDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getFreeNotiDetail(token, id)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    freeNoticeDetailModel.value = it
                }
        }
    }

    fun reportFreeNotice(
        token: String,
        body: ReportModel
    ) {
        viewModelScope.launch {
            repo.reportFreeNotice(token, body)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        callbackSuccess.value = Unit
                        callbackReportSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }
}