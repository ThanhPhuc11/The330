package com.nagaja.the330.view.applycompanyproduct

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.NameModel
import com.nagaja.the330.model.ProductModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProductCompanyVM(
    private val repo: ProductCompanyRepo
) : BaseViewModel() {

    //info company
    val textStateNameEng = mutableStateOf(TextFieldValue(""))
    val textStateNamePhi = mutableStateOf(TextFieldValue(""))
    val textStateNameKr = mutableStateOf(TextFieldValue(""))
    val textStateNameCN = mutableStateOf(TextFieldValue(""))
    val textStateNameJP = mutableStateOf(TextFieldValue(""))

    val textStatePeso = mutableStateOf(TextFieldValue(""))
    val textStateDollar = mutableStateOf(TextFieldValue(""))
    val textStateWon = mutableStateOf(TextFieldValue(""))

    val textStateDesEng = mutableStateOf(TextFieldValue(""))
    val textStateDesPhi = mutableStateOf(TextFieldValue(""))
    val textStateDesKr = mutableStateOf(TextFieldValue(""))
    val textStateDesCN = mutableStateOf(TextFieldValue(""))
    val textStateDesJP = mutableStateOf(TextFieldValue(""))

    var fileName = ""
    var filePath = ""


    fun makeCompany(token: String) {
//        val companyModel = CompanyModel().apply {
//            name = mutableListOf<NameAreaModel>().apply {
//                add(NameAreaModel(name = textStateNameEng.value.text, lang = AppConstants.Lang.EN))
//                add(NameAreaModel(name = textStateNamePhi.value.text, lang = AppConstants.Lang.PH))
//                add(NameAreaModel(name = textStateNameKr.value.text, lang = AppConstants.Lang.KR))
//                add(NameAreaModel(name = textStateNameCN.value.text, lang = AppConstants.Lang.CN))
//                add(NameAreaModel(name = textStateNameJP.value.text, lang = AppConstants.Lang.JP))
//            }
//            address = textStateAdress.value.text
//            description = mutableListOf<NameAreaModel>().apply {
//                add(NameAreaModel(name = textStateDesEng.value.text, lang = AppConstants.Lang.EN))
//                add(NameAreaModel(name = textStateDesPhi.value.text, lang = AppConstants.Lang.PH))
//                add(NameAreaModel(name = textStateDesKr.value.text, lang = AppConstants.Lang.KR))
//                add(NameAreaModel(name = textStateDesCN.value.text, lang = AppConstants.Lang.CN))
//                add(NameAreaModel(name = textStateDesJP.value.text, lang = AppConstants.Lang.JP))
//            }
//            chargeName = textStateName.value.text
//            chargePhone = textStatePhone.value.text
//            chargeEmail = textStateMailAddress.value.text
//            chargeFacebook = textStateFb.value.text
//            chargeKakao = textStateKakao.value.text
//            chargeLine = textStateLine.value.text
//
//            openHour = textStateOpenTime.value
//            closeHour = textStateCloseTime.value
//
//            file = fileName
//        }
        if (textStateNameEng.value.text.isBlank() ||
            textStateDesEng.value.text.isBlank() ||
            textStatePeso.value.text.isBlank() ||
            textStateDollar.value.text.isBlank()
        ) return
        val productModel = ProductModel().apply {
            name = mutableListOf<NameModel>().apply {
                add(NameModel(name = textStateNameEng.value.text, lang = AppConstants.Lang.EN))
                add(NameModel(name = textStateNamePhi.value.text, lang = AppConstants.Lang.PH))
                add(NameModel(name = textStateNameKr.value.text, lang = AppConstants.Lang.KR))
                add(NameModel(name = textStateNameCN.value.text, lang = AppConstants.Lang.CN))
                add(NameModel(name = textStateNameJP.value.text, lang = AppConstants.Lang.JP))
            }
            description = mutableListOf<NameModel>().apply {
                add(NameModel(name = textStateDesEng.value.text, lang = AppConstants.Lang.EN))
                add(NameModel(name = textStateDesPhi.value.text, lang = AppConstants.Lang.PH))
                add(NameModel(name = textStateDesKr.value.text, lang = AppConstants.Lang.KR))
                add(NameModel(name = textStateDesCN.value.text, lang = AppConstants.Lang.CN))
                add(NameModel(name = textStateDesJP.value.text, lang = AppConstants.Lang.JP))
            }

            peso = textStatePeso.value.text.toDouble()
            dollar = textStateDollar.value.text.toDouble()
            won = textStateWon.value.text.toDouble()
        }
        val companyModel = CompanyModel().apply {
            products = mutableListOf<ProductModel>().apply {
                add(productModel)
            }
        }
        viewModelScope.launch {
            repo.makeCompany(token, companyModel)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    val url = it.file?.url
                    url?.let { it1 -> uploadImageTest(it1) }
                }
        }
    }

    fun uploadImageTest(url: String) {
        val requestFile: RequestBody =
            File(filePath).asRequestBody("application/octet-stream".toMediaTypeOrNull())

        viewModelScope.launch {
            repo.uploadImage(url, requestFile)
                .onStart { }
                .onCompletion { }
                .catch {
                    Log.e("Catch", this.toString())
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        Log.e("Success", "upload success")
                    } else {
                        Log.e("Fail", "upload fail")
                    }
                }
        }
    }
}