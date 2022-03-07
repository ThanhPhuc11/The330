package com.nagaja.the330.view.reportmissing

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.ReportMissingModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReportMissingVM(
    private val repo: ReportMissingRepo
) : BaseViewModel() {
    var listDataReport = mutableListOf<ReportMissingModel>()
    var stateListDataReport = mutableStateListOf<ReportMissingModel>()

    var listDataMissing = mutableListOf<ReportMissingModel>()
    var stateListDataMissing = mutableStateListOf<ReportMissingModel>()


    fun getListLocalNews(
        token: String,
//        page: Int,
//        size: Int,
        sort: String = "LASTEST",
        type: String = "REPORT"
    ) {
        viewModelScope.launch {
            repo.getReportMissingList(
                token = token,
                page = 0,
                size = 10,
                sort = sort,
                type = type,
                null
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    listDataReport.clear()
                    it.content?.let { it1 ->
                        if (type == "REPORT") {
                            listDataReport.addAll(it1)
                            stateListDataReport.clear()
                            stateListDataReport.addAll(listDataReport)
                        } else {
                            listDataMissing.addAll(it1)
                            stateListDataMissing.clear()
                            stateListDataMissing.addAll(listDataReport)
                        }
                    }
                }
        }
    }
}