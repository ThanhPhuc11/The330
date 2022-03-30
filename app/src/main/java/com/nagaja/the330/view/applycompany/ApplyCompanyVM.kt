package com.nagaja.the330.view.applycompany

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.*
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


class ApplyCompanyVM(
    private val repo: ApplyCompanyRepo
) : BaseViewModel() {
    val listCategoryState = mutableStateListOf<CategoryModel>()
    val selectedOptionCategory = mutableStateOf(CategoryModel())

    val listImageRepresentative = mutableStateListOf<FileModel>()
    val listPopularAreas = mutableStateListOf<DistrictModel>()
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

    var popularAreaId: Int? = null
    var cityId: Int? = null
    var districtId: Int? = null

    var serviceType = mutableListOf<ChooseKeyValue>().apply {
        add(ChooseKeyValue(AppConstants.DELIVERY, "", false))
        add(ChooseKeyValue(AppConstants.RESERVATION, "", false))
        add(ChooseKeyValue(AppConstants.PICKUP_DROP, "", false))
    }

    //info company
    val textStateNameEng = mutableStateOf(TextFieldValue(""))
    val textStateNamePhi = mutableStateOf(TextFieldValue(""))
    val textStateNameKr = mutableStateOf(TextFieldValue(""))
    val textStateNameCN = mutableStateOf(TextFieldValue(""))
    val textStateNameJP = mutableStateOf(TextFieldValue(""))

    val textStateAdress = mutableStateOf(TextFieldValue(""))

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

    val textStateOpenTime = mutableStateOf(-1)
    val textStateCloseTime = mutableStateOf(-1)

    var reservationTime: MutableList<Int>? = mutableListOf()

    val textStateNumReservation = mutableStateOf(TextFieldValue(""))
    val textStatePaymethod = mutableStateOf(TextFieldValue(""))

    var fileName = ""
    var filePath = "" //TODO: path sau khi lay realpath
    var totalFileUpload = 0
    var fileUploaded = 0

    val callbackMakeSuccess = MutableLiveData<Unit>()

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

    fun isValidate(): Boolean {
        if (selectedOptionCategory.value.ctype.isNullOrBlank() ||
            textStateNameEng.value.text.isBlank() ||
            textStateAdress.value.text.isBlank() ||
            textStateDesEng.value.text.isBlank() ||
            textStateName.value.text.isBlank() ||
            textStateOpenTime.value < 0 ||
            textStateCloseTime.value < 0 ||
            fileName.isEmpty() ||
            (serviceType[1].isSelected && reservationTime.isNullOrEmpty()) ||
            (serviceType[1].isSelected && textStateNumReservation.value.text.isBlank())
        ) {
            showMessCallback.value = "A field required not fill"
            return false
        }
        return true
    }

    fun saveCompanyTransfer(): CompanyModel {
        return CompanyModel().apply {
            ctype = selectedOptionCategory.value.ctype
            images = listImageRepresentative.onEachIndexed { index, obj ->
                obj.priority = index
            }
            name = mutableListOf<NameModel>().apply {
                add(NameModel(name = textStateNameEng.value.text, lang = AppConstants.Lang.EN))
                add(NameModel(name = textStateNamePhi.value.text, lang = AppConstants.Lang.PH))
                add(NameModel(name = textStateNameKr.value.text, lang = AppConstants.Lang.KR))
                add(NameModel(name = textStateNameCN.value.text, lang = AppConstants.Lang.CN))
                add(NameModel(name = textStateNameJP.value.text, lang = AppConstants.Lang.JP))
            }
            popularAreaId = this@ApplyCompanyVM.popularAreaId
            cityId = this@ApplyCompanyVM.cityId
            districtId = this@ApplyCompanyVM.districtId
            address = textStateAdress.value.text
            description = mutableListOf<NameModel>().apply {
                add(NameModel(name = textStateDesEng.value.text, lang = AppConstants.Lang.EN))
                add(NameModel(name = textStateDesPhi.value.text, lang = AppConstants.Lang.PH))
                add(NameModel(name = textStateDesKr.value.text, lang = AppConstants.Lang.KR))
                add(NameModel(name = textStateDesCN.value.text, lang = AppConstants.Lang.CN))
                add(NameModel(name = textStateDesJP.value.text, lang = AppConstants.Lang.JP))
            }
            chargeName = textStateName.value.text
            chargePhone = textStatePhone.value.text
            chargeEmail = textStateMailAddress.value.text
            chargeFacebook = textStateFb.value.text
            chargeKakao = textStateKakao.value.text
            chargeLine = textStateLine.value.text

            serviceTypes = this@ApplyCompanyVM.serviceType.filter {
                it.isSelected
            }.map {
                it.id!!
            }.toMutableList()

            openHour = textStateOpenTime.value
            closeHour = textStateCloseTime.value

            reservationTime = this@ApplyCompanyVM.reservationTime
            reservationNumber = textStateNumReservation.value.text.ifBlank { null }?.toInt()
            paymentMethod = textStatePaymethod.value.text.ifBlank { null }

            file = fileName
            fileTemp = FileModel(url = filePath)
        }
    }

    fun makeCompany(token: String) {
        if (!isValidate()) return
        val companyModel = saveCompanyTransfer()
        viewModelScope.launch {
            repo.makeCompany(token, companyModel)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    totalFileUpload = listImageRepresentative.size + 1
                    if (it.images != null && it.images!!.size > 0)
                        it.images?.onEach { fileModel ->
                            listImageRepresentative.forEach { fileLocal ->
                                if (fileModel.url?.contains(fileLocal.fileName!!) == true) {
                                    uploadImageTest(fileModel.url ?: "", fileLocal.url!!)
                                }
                            }
                        }

                    it.file?.url?.let { it1 -> uploadImageTest(it1, filePath) }
                }
        }
    }

    private fun uploadImageTest(url: String, path: String) {
        val requestFile: RequestBody =
            File(path).asRequestBody("application/octet-stream".toMediaTypeOrNull())

        viewModelScope.launch {
            repo.uploadImage(url, requestFile)
                .onStart { }
                .onCompletion { }
                .catch {
                    Log.e("Catch", this.toString() + path)
                    fileUploaded++
                    if (fileUploaded == totalFileUpload) {
                        callbackMakeSuccess.value = Unit
                    }
                }
                .collect {
                    fileUploaded++
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        Log.e("Success", path)
                    } else {
                        Log.e("Fail", path)
                    }
                    if (fileUploaded == totalFileUpload) {
                        callbackMakeSuccess.value = Unit
                    }
                }
        }
    }
}