package com.nagaja.the330.view.recruitment

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecruitmentJobSearchVM(
    private val repo: RecruitmentJobSearchRepo
) : BaseViewModel() {
    var sort = "LASTEST"
    var type = AppConstants.RECRUITMENT
    var listDataRecruitment = mutableListOf<RecruitmentJobsModel>()
    var stateListDataRecruitment = mutableStateListOf<RecruitmentJobsModel>()

    var listDataJobs = mutableListOf<RecruitmentJobsModel>()
    var stateListDataJobs = mutableStateListOf<RecruitmentJobsModel>()

    val existJobSearch = MutableLiveData<Boolean>()


    fun getRecruitmentList(
        token: String,
        page: Int,
        type: String
    ) {
        viewModelScope.launch {
            repo.getRecruitmentList(
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
                        if (type == AppConstants.RECRUITMENT) {
                            if (page == 0) stateListDataRecruitment.clear()
                            stateListDataRecruitment.addAll(it1)
                        } else {
                            if (page == 0) stateListDataJobs.clear()
                            stateListDataJobs.addAll(it1)
                        }
                    }
                }
        }
    }

    fun checkExistJobSearch(token: String) {
        viewModelScope.launch {
            repo.checkExistJobSearch(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    existJobSearch.value = it
                }
        }
    }
}