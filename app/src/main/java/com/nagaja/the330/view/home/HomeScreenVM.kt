package com.nagaja.the330.view.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeScreenVM(
    private val repo: HomeScreenRepo
) : BaseViewModel() {
    var token: String = ""

    //    val listCategoryState = mutableStateListOf<CategoryModel>()
    val listPopularAreas = mutableStateListOf<DistrictModel>()
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

    var popularAreaId: Int? = null
    var cityId: Int? = null
    var districtId: Int? = null

    val listCategoryState = mutableStateOf(mutableListOf<CategoryModel>())
    val statelistCompany = mutableStateListOf<CompanyRecommendModel>()
    val stateListBanner = mutableStateListOf<BannerCompanyModel>()
    val stateCompanyFooter = mutableStateOf(CompanyConfigInfo())
    fun getCategory(token: String, group: String?) {
        viewModelScope.launch {
            repo.getCategory(token, group)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 -> listCategoryState.value = it1 }
//                    it.content?.let { it1 -> listCategoryState.addAll(it1) }
                }
        }
    }

    fun getCompanyRecommendAds(token: String) {
        viewModelScope.launch {
            repo.getCompanyRecommendAds(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 ->
                        statelistCompany.clear()
                        statelistCompany.addAll(it1)
                    }
                }
        }
    }

    fun registerFCM(token: String, tokenFCM: TokenFCMRequest) {
        viewModelScope.launch {
            repo.registerFCM(token, tokenFCM)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 201) {
                        callbackSuccess.value = Unit
                    }
                }
        }
    }

    fun getBanner(token: String) {
        viewModelScope.launch {
            repo.getBanner(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 -> stateListBanner.addAll(it1) }
                }
        }
    }

    fun getConfigCompanyInfo(token: String) {
        viewModelScope.launch {
            repo.getConfigCompanyInfo(token)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    stateCompanyFooter.value = it
                }
        }
    }

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

    fun getPopularAreas(token: String) {
        viewModelScope.launch {
            repo.getPopularAreas(token)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 ->
                        listPopularAreas.clear()
                        listPopularAreas.addAll(it1)
                    }
                }
        }
    }
}