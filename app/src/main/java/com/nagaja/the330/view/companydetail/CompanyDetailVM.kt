package com.nagaja.the330.view.companydetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.RecruitmentJobsModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CompanyDetailVM(
    private val repo: CompanyDetailRepo
) : BaseViewModel() {
    var companyDetail = mutableStateOf(CompanyModel())


    fun getCompanyDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getCompanyDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    companyDetail.value = it
                }
        }
    }
}