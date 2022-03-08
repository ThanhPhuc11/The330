package com.nagaja.the330.view.recruitmentdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.model.ReportMissingModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecruitJobsDetailVM(
    private val repo: RecruitJobsDetailRepo
) : BaseViewModel() {
    var recruitJobsModel = mutableStateOf(RecruitmentJobsModel())


    fun getRecruitJobsDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getRecruitJobsDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    recruitJobsModel.value = it
                }
        }
    }
}