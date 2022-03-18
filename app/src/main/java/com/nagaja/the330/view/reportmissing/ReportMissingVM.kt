package com.nagaja.the330.view.reportmissing

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

class ReportMissingVM(
    private val repo: ReportMissingRepo
) : BaseViewModel() {
    var sort = "LASTEST"
    var type = AppConstants.REPORT

    var listDataReport = mutableListOf<ReportMissingModel>()
    var stateListDataReport = mutableStateListOf<ReportMissingModel>()

    var listDataMissing = mutableListOf<ReportMissingModel>()
    var stateListDataMissing = mutableStateListOf<ReportMissingModel>()


    fun getReportMissingList(
        token: String,
        page: Int,
        type: String = "REPORT"
    ) {
        viewModelScope.launch {
            repo.getReportMissingList(
                token = token,
                page = page,
                size = 20,
                sort = sort,
                type = type,
                null
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 ->
                        if (type == AppConstants.REPORT) {
                            listDataReport.clear()
                            listDataReport.addAll(it1)
                            stateListDataReport.addAll(listDataReport)
                        } else {
                            listDataMissing.clear()
                            listDataMissing.addAll(it1)
                            stateListDataMissing.addAll(listDataReport)
                        }
                    }
                }
        }
    }
}