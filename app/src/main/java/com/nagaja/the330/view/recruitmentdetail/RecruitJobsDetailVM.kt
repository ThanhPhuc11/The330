package com.nagaja.the330.view.recruitmentdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.RecruitmentJobsModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecruitJobsDetailVM(
    private val repo: RecruitJobsDetailRepo
) : BaseViewModel() {
    var recruitJobsModel = mutableStateOf(RecruitmentJobsModel())
    var callbackDelSuccess = MutableLiveData<Unit>()
    var callbackConfirmSuccess = MutableLiveData<Unit>()
    var isCheck: Boolean = false


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
                    checkConfirm(token, id)
                }
        }
    }

    fun cancelPostRecruitJobs(token: String) {
        viewModelScope.launch {
            repo.editPostRecruitJobs(token, RecruitmentJobsModel().apply {
                id = recruitJobsModel.value.id
                status = "DELETED"
            })
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    callbackDelSuccess.value = Unit
                }
        }
    }

    fun checkConfirm(token: String, recruitmentJobId: Int) {
        viewModelScope.launch {
            repo.checkConfirm(token, recruitmentJobId)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    isCheck = it
                }
        }
    }

    fun confirmRecruit(token: String) {
        if (isCheck) return
        viewModelScope.launch {
            repo.confirmRecruit(token, recruitJobsModel.value.id!!)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        callbackSuccess.value = Unit
                        callbackConfirmSuccess.value = Unit
                    } else {
                        handleError2(it)
                    }
                }
        }
    }


}