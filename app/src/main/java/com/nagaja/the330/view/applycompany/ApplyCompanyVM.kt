package com.nagaja.the330.view.applycompany

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.nagaja.the330.base.BaseViewModel

class ApplyCompanyVM(
    private val repo: ApplyCompanyRepo
) : BaseViewModel() {
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
}