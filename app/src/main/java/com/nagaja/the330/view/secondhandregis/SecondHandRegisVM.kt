package com.nagaja.the330.view.secondhandregis

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CityModel
import com.nagaja.the330.model.DistrictModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SecondHandRegisVM(
    private val repo: SecondHandRegisRepo
) : BaseViewModel() {
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

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

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}