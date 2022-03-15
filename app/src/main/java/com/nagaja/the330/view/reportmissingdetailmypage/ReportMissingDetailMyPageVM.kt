package com.nagaja.the330.view.reportmissingdetailmypage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ReportMissingModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReportMissingDetailMyPageVM(
    private val repo: ReportMissingDetailMyPageRepo
) : BaseViewModel() {
    var reportMissingModel = mutableStateOf(ReportMissingModel())


    fun getLocalNewsDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getReportMissingDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    reportMissingModel.value = it
                }
        }
    }
}