package com.nagaja.the330.view.reportmissingmypage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ReportMissingModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReportMissingMyPageVM(
    private val repo: ReportMissingMyPageRepo
) : BaseViewModel() {
    var timeLimit = "ONE_WEEK"
    var type: String? = AppConstants.REPORT
    var status: String? = null

    var listDataReport = mutableListOf<ReportMissingModel>()
    var stateListDataReport = mutableStateListOf<ReportMissingModel>()


    fun getReportMissingMyPage(
        token: String,
        page: Int
    ) {
        viewModelScope.launch {
            repo.getReportMissingMyPage(
                token = token,
                page = page,
                size = 20,
                timeLimit = timeLimit,
                type = null,
                status = status,
                null
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 ->
                        listDataReport.clear()
                        listDataReport.addAll(it1)
                        stateListDataReport.clear()
                        stateListDataReport.addAll(listDataReport)
                    }
                }
        }
    }
}