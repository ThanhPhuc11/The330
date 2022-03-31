package com.nagaja.the330.view.editcompanyproduct

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.NameModel
import com.nagaja.the330.model.ProductModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class EditProductCompanyVM(
    private val repo: EditProductCompanyRepo
) : BaseViewModel() {
    //    val companyModel = mutableStateOf(CompanyModel())
    var companyId = 0
    val listAdmin = mutableStateListOf<String>()
    val currentProductInfo = mutableStateOf(ProductModel())
    val listImageProduct = mutableStateListOf<FileModel>()

    val mapTotalImage = mutableMapOf<String, FileModel>()
    val listProductValidate = mutableStateListOf<ProductModel>()

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

    var totalFileUpload = 0
    var fileUploaded = 0
    val stateCheckOk = mutableStateOf(false)
    val callbackCheckNotOk = MutableLiveData<Unit>()
    val callbackEditSuccess = MutableLiveData<Unit>()

    fun addProductToList() {
        if (textStateNameEng.value.text.isBlank() ||
            textStateDesEng.value.text.isBlank() ||
            textStatePeso.value.text.isBlank() ||
            textStateDollar.value.text.isBlank()
        ) {
            showMessCallback.value = "A field required not fill"
            return
        }
        val listTemp = mutableListOf<FileModel>().apply { addAll(listImageProduct) }
        val obj = ProductModel().apply {
            images = listTemp.onEachIndexed { index, obj ->
                obj.priority = index
            }
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
            if (textStateWon.value.text.isNotEmpty()) {
                won = textStateWon.value.text.toDouble()
            }
        }
        val currentIndex = listProductValidate.indexOfFirst { it.id == currentProductInfo.value.id }
        listProductValidate.removeAt(currentIndex)
        listProductValidate.add(currentIndex, currentProductInfo.value.copy())
        refreshInput()
        listProductValidate.add(ProductModel())
        currentProductInfo.value = ProductModel()
    }

    private fun refreshInput() {
        listImageProduct.clear()

        textStateNameEng.value = TextFieldValue("")
        textStateNamePhi.value = TextFieldValue("")
        textStateNameKr.value = TextFieldValue("")
        textStateNameCN.value = TextFieldValue("")
        textStateNameJP.value = TextFieldValue("")

        textStatePeso.value = TextFieldValue("")
        textStateDollar.value = TextFieldValue("")
        textStateWon.value = TextFieldValue("")

        textStateDesEng.value = TextFieldValue("")
        textStateDesPhi.value = TextFieldValue("")
        textStateDesKr.value = TextFieldValue("")
        textStateDesCN.value = TextFieldValue("")
        textStateDesJP.value = TextFieldValue("")

        fileName = ""
    }

    fun saveProductCurrent(): Boolean {
        if (textStateNameEng.value.text.isBlank() ||
            textStateDesEng.value.text.isBlank() ||
            textStatePeso.value.text.isBlank() ||
            textStateDollar.value.text.isBlank()
        ) {
            showMessCallback.value = "A field required not fill"
            return false
        }

        currentProductInfo.value.apply {
            images = listImageProduct.onEachIndexed { index, obj ->
                obj.priority = index
            }
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
            if (textStateWon.value.text.isNotEmpty()) {
                won = textStateWon.value.text.toDouble()
            }
        }

        val currentIndex = listProductValidate.indexOfFirst { it.id == currentProductInfo.value.id }
        listProductValidate.removeAt(currentIndex)
        listProductValidate.add(currentIndex, currentProductInfo.value.copy())
        return true
    }

    fun editCompany(token: String) {
        if (!saveProductCurrent()) return

        val companyObj = CompanyModel().apply {
            id = companyId
            products = listProductValidate.onEachIndexed { index, obj ->
                obj.priority = index
            }
            adminNames = listAdmin
        }
        return
        viewModelScope.launch {
            repo.editCompany(token, companyObj)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit

                    it.products!!.forEach { it2 ->
                        it2.images!!.forEach { _ ->
                            totalFileUpload++
                        }
                    }
                    totalFileUpload += it.images!!.size
                    if (fileUploaded == totalFileUpload) {
                        callbackEditSuccess.value = Unit
                    }
                    if (it.products != null && it.products!!.size > 0)
                        it.products!!.forEach { productModel ->
                            productModel.images!!.forEach { fileModel ->
                                val key = fileModel.suffixUrl!!.split("/").last()
                                uploadImageTest(fileModel.url ?: "", mapTotalImage[key]?.url!!)
                                Log.e("key", fileModel.suffixUrl!!)
                            }
                        }
                }
        }
    }

    fun checkValidateAdminName(token: String, name: String) {
        viewModelScope.launch {
            repo.checkValidateAdminName(token, name)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (it) {
                        stateCheckOk.value = true
                    } else {
                        callbackCheckNotOk.value = Unit
                    }
                }
        }
    }

    private fun uploadImageTest(url: String, path: String) {
        val requestFile: RequestBody =
            File(path).asRequestBody("application/octet-stream".toMediaTypeOrNull())

        viewModelScope.launch {
            repo.uploadImage(url, requestFile)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                    Log.e("Catch", this.toString())
                    fileUploaded++
                    if (fileUploaded == totalFileUpload) {
                        callbackEditSuccess.value = Unit
                    }
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        Log.e("Success", "upload success")
                    } else {
                        Log.e("Fail", "upload fail")
                    }

                    fileUploaded++
                    if (fileUploaded == totalFileUpload) {
                        callbackEditSuccess.value = Unit
                    }
                }
        }
    }
}