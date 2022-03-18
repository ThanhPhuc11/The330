package com.nagaja.the330.view.secondhandmarket

import androidx.compose.runtime.mutableStateListOf
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

class SecondHandMarketVM(
    private val repo: SecondHandMarketRepo
) : BaseViewModel() {
    var category: String? = null
    var sort: String = "LASTEST"
    var city: String? = null
    var district: String? = null
    var listData = mutableListOf<SecondHandModel>()
    var stateListData = mutableStateListOf<SecondHandModel>()


    val listImage = mutableStateListOf<FileModel>()
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

    val callbackPostSuccess = MutableLiveData<Unit>()

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

    fun getListSecondHandMarket(
        token: String,
        page: Int
    ) {
        viewModelScope.launch {
            repo.secondHandMarket(
                token = token,
                cityId = city?.toInt(),
                districtId = district?.toInt(),
                secondhandCategoryType = category,
                page = page,
                size = 20,
                sort = sort
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        listData.clear()
                    }
                    it.content?.let { it1 ->
                        listData.addAll(it1)
                        stateListData.addAll(listData)
                    }
                }
        }
    }
}