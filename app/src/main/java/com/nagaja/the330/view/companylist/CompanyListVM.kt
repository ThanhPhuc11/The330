package com.nagaja.the330.view.companylist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.BannerCompanyModel
import com.nagaja.the330.model.CompanyModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CompanyListVM(
    private val repo: CompanyListRepo
) : BaseViewModel() {
    var keyword: String? = null
    var sort: String = "NAGAJA_RECOMMEND_ORDER"
    var filter: MutableList<String>? = null
    var cType: String? = null
    var authentication: Boolean? = null
    val listData = mutableListOf<CompanyModel>()
    val stateListData = mutableStateListOf<CompanyModel>()
    val stateListBanner = mutableStateListOf<BannerCompanyModel>()

    fun findCompany(
        token: String,
        page: Int,
        latitude: Float? = null,
        longitude: Float? = null,
    ) {
        viewModelScope.launch {
            repo.findCompany(
                token,
                page,
                size = 20,
                sort,
                filter,
                cType,
                authentication,
                latitude,
                longitude,
                keyword
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) stateListData.clear()
                    it.content?.let { data ->
                        stateListData.addAll(data)
                    }
                }
        }
    }

    fun getBanner(token: String) {
        viewModelScope.launch {
            repo.getBanner(token, cType ?: "")
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    it.content?.let { it1 -> stateListBanner.addAll(it1) }
                }
        }
    }
}