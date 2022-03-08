package com.nagaja.the330.view.recruitment

import androidx.compose.runtime.mutableStateListOf
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


    fun getRecruitmentList(
        token: String,
//        page: Int,
//        size: Int,
        type: String
    ) {
        viewModelScope.launch {
            repo.getRecruitmentList(
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
                    it.content?.let { it1 ->
                        if (type == AppConstants.RECRUITMENT) {
                            listDataRecruitment.clear()
                            listDataRecruitment.addAll(it1)
                            stateListDataRecruitment.clear()
                            stateListDataRecruitment.addAll(listDataRecruitment)
                        } else {
                            listDataJobs.clear()
                            listDataJobs.addAll(it1)
                            stateListDataJobs.clear()
                            stateListDataJobs.addAll(listDataJobs)
                        }
                    }
                }
        }
    }
}