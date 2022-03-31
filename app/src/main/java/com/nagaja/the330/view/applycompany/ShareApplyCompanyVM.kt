package com.nagaja.the330.view.applycompany

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.ProductModel

class ShareApplyCompanyVM: ViewModel() {
    val companyInfoState = mutableStateOf(CompanyModel())
    val productsOfCompany = mutableListOf<ProductModel>()
    val listAdmin = mutableListOf<String>()
    var companyId = 0

    override fun onCleared() {
        super.onCleared()
        Log.e("onCleared", "onCleared")
    }
}