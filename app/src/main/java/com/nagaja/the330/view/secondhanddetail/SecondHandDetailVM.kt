package com.nagaja.the330.view.secondhanddetail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CityModel
import com.nagaja.the330.model.DistrictModel
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.SecondHandModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SecondHandDetailVM(
    private val repo: SecondHandDetailRepo
) : BaseViewModel() {
    val secondhandDetail = mutableStateOf(SecondHandModel())
    val listImage = mutableStateListOf<FileModel>()
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

    val callbackPostSuccess = MutableLiveData<Unit>()
    val callbackDelOrCompletedSuccess = MutableLiveData<Unit>()

    fun getCity(token: String) {
        viewModelScope.launch {
            repo.getCity(token)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 -> listCity.addAll(it1) }
                }
        }
    }

    fun getDistrict(token: String, cityId: Int) {
        viewModelScope.launch {
            repo.getDistrict(token, cityId)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 ->
                        listDistrict.clear()
                        listDistrict.addAll(it1)
                    }
                }
        }
    }

    fun getDetail(token: String, id: Int) {
        viewModelScope.launch {
            repo.viewDetailSecondhandPost(token, id)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    secondhandDetail.value = it
                }
        }
    }

    fun completeOrDel(token: String, isDel: Boolean) {
        viewModelScope.launch {
            repo.editSecondhandPost(token, mutableListOf<SecondHandModel>().apply {
                add(SecondHandModel().apply {
                    id = secondhandDetail.value.id
                    if (isDel) {
                        status = "DELETED"
                    } else {
                        transactionStatus = "TRANSACTION_COMPLETE"
                    }
                })
            })
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    callbackDelOrCompletedSuccess.value = Unit
                }
        }
    }
}