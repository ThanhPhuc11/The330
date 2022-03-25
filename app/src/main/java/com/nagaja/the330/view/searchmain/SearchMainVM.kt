package com.nagaja.the330.view.searchmain

import androidx.compose.runtime.mutableStateOf
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.utils.AppConstants

class SearchMainVM(
    private val repo: SearchMainRepo
) : BaseViewModel() {
    val stateCategoryType = mutableStateOf(AppConstants.SearchTitle.COMPANY)
    var keyword = mutableStateOf<String?>(null)
}