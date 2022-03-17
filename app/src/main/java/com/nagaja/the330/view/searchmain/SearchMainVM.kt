package com.nagaja.the330.view.searchmain

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.*
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchMainVM(
    private val repo: SearchMainRepo
) : BaseViewModel() {
    val stateCategoryType = mutableStateOf(AppConstants.SearchTitle.COMPANY)
    var keyword: String? = null

    val stateListCompany = mutableStateListOf<CompanyModel>()
    val stateListFreeNotice = mutableStateListOf<FreeNoticeModel>()
    val stateListRecruitJobs = mutableStateListOf<RecruitmentJobsModel>()
    val stateListSecondHand = mutableStateListOf<SecondHandModel>()
    val stateListReportMissing = mutableStateListOf<ReportMissingModel>()

    fun findCompany(
        token: String,
        page: Int = 0,
        size: Int = 10,
        cityId: Int? = null,
        districtId: Int? = null,
    ) {
        viewModelScope.launch {
            repo.findCompany(
                token,
                page,
                size,
                "REGULAR_ORDER",
                null,
                null,
                null,
                cityId,
                districtId,
                keyword
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { data ->
                        stateListCompany.clear()
                        stateListCompany.addAll(data)
                    }
                }
        }
    }

    fun getFreeNoticeBoard(
        token: String,
//        page: Int,
//        size: Int,
        sort: String = "LASTEST",
    ) {
        viewModelScope.launch {
            repo.getFreeNoticeBoard(
                token = token,
                page = 0,
                size = 10,
                sort = sort,
                all = keyword
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 ->
                        stateListFreeNotice.clear()
                        stateListFreeNotice.addAll(it1)
                    }
                }
        }
    }

    fun getRecruitmentList(
        token: String,
//        page: Int,
//        size: Int,
        type: String = "RECRUITMENT",
    ) {
        viewModelScope.launch {
            repo.getRecruitmentList(
                token = token,
                page = 0,
                size = 10,
                sort = "LASTEST",
                type = type,
                all = keyword
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 ->
                        stateListRecruitJobs.clear()
                        stateListRecruitJobs.addAll(it1)
                    }
                }
        }
    }
}