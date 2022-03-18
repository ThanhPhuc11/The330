package com.nagaja.the330.view.point

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.PointModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PointVM(
    private val repo: PointRepo,
) : BaseViewModel() {
    var timeLimit = "ONE_DAY"
    var chargeCount = mutableStateOf(0)
    var usageCount = mutableStateOf(0)
    val stateListCharge = mutableStateListOf<PointModel>()
    val stateListUsage = mutableStateListOf<PointModel>()

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
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    companyDetail.value = it
                }
        }
    }

    fun getPoints(
        token: String,
        page: Int,
        pointTransactionType: String
    ) {
        viewModelScope.launch {
            repo.getPoints(token, page, size = 20, pointTransactionType, timeLimit)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (pointTransactionType == AppConstants.Point.PROVIDED) {
                        if (page == 0) {
                            stateListCharge.clear()
                            chargeCount.value = it.totalElements?:0
                        }
                        it.content?.let { data ->
                            stateListCharge.addAll(data)
                        }
                    } else {
                        if (page == 0) {
                            stateListUsage.clear()
                            usageCount.value = it.totalElements?:0
                        }
                        it.content?.let { data ->
                            stateListUsage.addAll(data)
                        }
                    }
                }
        }
    }
}