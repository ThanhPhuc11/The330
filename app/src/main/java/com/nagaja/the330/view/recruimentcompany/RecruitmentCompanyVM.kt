package com.nagaja.the330.view.recruimentcompany

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.RecruitmentJobsModel
import com.nagaja.the330.model.RoomDetailModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecruitmentCompanyVM(
    private val repo: RecruitmentCompanyRepo
) : BaseViewModel() {
    var timeLimit = ""
    val stateListRecuitment = mutableStateListOf<RecruitmentJobsModel>()
    val stateListRoom = mutableStateListOf<RoomDetailModel>()
    val totalCase = mutableStateOf(0)

    fun getRecruitmentMypage(token: String, page: Int) {
        viewModelScope.launch {
            repo.getRecruitmentMypage(token, page, 20, timeLimit = timeLimit)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListRecuitment.clear()
                        totalCase.value = it.totalElements ?: 0
                    }
                    it.content?.let { it1 ->
                        stateListRecuitment.addAll(it1)
                    }
                }
        }
    }

    fun getChatList(
        token: String,
        page: Int,
    ) {
        viewModelScope.launch {
            repo.getChatList(token, page, size = 20, "RECRUITMENT", typeSearchChat = "ONE_YEAR")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListRoom.clear()
                    }
                    it.content?.let { data ->
                        stateListRoom.addAll(data)
                    }
                }
        }
    }
}