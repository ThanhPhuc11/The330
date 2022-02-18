package com.nagaja.the330.view.applycompany

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nagaja.the330.model.CompanyModel

class ShareApplyCompanyVM: ViewModel() {
    val companyInfoState = mutableStateOf(CompanyModel())

    override fun onCleared() {
        super.onCleared()
        Log.e("onCleared", "onCleared")
    }
}