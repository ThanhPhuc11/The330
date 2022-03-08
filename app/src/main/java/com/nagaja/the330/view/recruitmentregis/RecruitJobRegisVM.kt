package com.nagaja.the330.view.recruitmentregis

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CityModel
import com.nagaja.the330.model.DistrictModel
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.model.ReportMissingModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RecruitJobRegisVM(
    private val repo: RecruitJobRegisRepo
) : BaseViewModel() {
    val listImage = mutableStateListOf<FileModel>()
    val listCity = mutableStateListOf<CityModel>()
    val listDistrict = mutableStateListOf<DistrictModel>()

    val stateEdtLocalDes = mutableStateOf(TextFieldValue(""))
    val stateEdtTitle = mutableStateOf(TextFieldValue(""))
    val stateEdtBody = mutableStateOf(TextFieldValue(""))
    val stateTypeReport = mutableStateOf("REPORT")
    val stateEdtSNSInfo = mutableStateOf(TextFieldValue(""))

    var city: String? = null
    var district: String? = null

    private var countImageUpload = 0
    private var countImageUploadDone = 0
    private var justSuccessId: Int? = null

    val callbackPostSuccess = MutableLiveData<Int>()

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

    fun makePostReportMissing(token: String) {
        if (stateEdtTitle.value.text.isBlank() || stateEdtBody.value.text.isBlank()) return
        val body = ReportMissingModel().apply {
            title = stateEdtTitle.value.text
            body = stateEdtBody.value.text
            type = stateTypeReport.value
            images = listImage
            locationDescription = stateEdtLocalDes.value.text
        }
        viewModelScope.launch {
            repo.makePostReportMissing(token, body)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    justSuccessId = it.id
                    if (it.presignedList?.isNotEmpty() == true) {
                        countImageUpload = it.presignedList!!.size
                        it.presignedList!!.forEach { fileModel ->
                            listImage.forEach { fileLocal ->
                                if (fileModel.url?.contains(fileLocal.fileName!!) == true) {
                                    uploadImageTest(
                                        url = fileModel.url ?: "",
                                        path = fileLocal.url!!
                                    )
                                }
                            }
                        }
                    } else {
                        callbackPostSuccess.value = justSuccessId!!
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
                    Log.e("Catch", this.toString() + path)
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    countImageUploadDone++
                    if (it.raw().isSuccessful && it.raw().code == 200) {
                        Log.e("Success", path)
                    } else {
                        Log.e("Fail", path)
                        handleError2(it)
                    }
                    if (countImageUploadDone == countImageUpload) {
                        callbackPostSuccess.value = justSuccessId!!
                    }
                }
        }
    }
}