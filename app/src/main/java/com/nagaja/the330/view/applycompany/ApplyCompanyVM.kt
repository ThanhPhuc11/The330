package com.nagaja.the330.view.applycompany

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CategoryModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ApplyCompanyVM(
    private val repo: ApplyCompanyRepo
) : BaseViewModel() {
    val listCategoryState = mutableStateListOf<CategoryModel>()

    //info company
    val textStateNameEng = mutableStateOf(TextFieldValue(""))
    val textStateNamePhi = mutableStateOf(TextFieldValue(""))
    val textStateNameKr = mutableStateOf(TextFieldValue(""))
    val textStateNameCN = mutableStateOf(TextFieldValue(""))
    val textStateNameJP = mutableStateOf(TextFieldValue(""))

    val textStateDesEng = mutableStateOf(TextFieldValue(""))
    val textStateDesPhi = mutableStateOf(TextFieldValue(""))
    val textStateDesKr = mutableStateOf(TextFieldValue(""))
    val textStateDesCN = mutableStateOf(TextFieldValue(""))
    val textStateDesJP = mutableStateOf(TextFieldValue(""))

    //info person
    val textStateName = mutableStateOf(TextFieldValue(""))
    val textStatePhone = mutableStateOf(TextFieldValue(""))
    val textStateMailAddress = mutableStateOf(TextFieldValue(""))
    val textStateFb = mutableStateOf(TextFieldValue(""))
    val textStateKakao = mutableStateOf(TextFieldValue(""))
    val textStateLine = mutableStateOf(TextFieldValue(""))

    val textStateNumReservation = mutableStateOf(TextFieldValue(""))

    fun getCategory(token: String) {
        viewModelScope.launch {
            repo.getCategory(token, "COMPANY_INFO")
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 -> listCategoryState.addAll(it1) }
                }
        }
    }

    fun makeCompany(token: String) {
        val companyModel = CompanyModel().apply {

        }
        viewModelScope.launch {
            repo.makeCompany(token, companyModel)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {

                }
        }
    }
}