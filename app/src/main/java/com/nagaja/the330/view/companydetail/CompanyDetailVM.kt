package com.nagaja.the330.view.companydetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CompanyDetailVM(
    private val repo: CompanyDetailRepo
) : BaseViewModel() {
    var companyDetail = mutableStateOf(CompanyModel())
    var isLike = mutableStateOf(false)
    var isRecommend = mutableStateOf(false)
    var isCloseToday = mutableStateOf(false)


    fun getCompanyDetail(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            repo.getCompanyDetail(
                token = token, id
            )
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    companyDetail.value = it
                    checkLikeCompany(token)
                    checkRecommendCompany(token)
                    checkCloseToday(token, id)
                }
        }
    }

    fun checkCloseToday(token: String, companyId: Int) {
        viewModelScope.launch {
            repo.checkCloseToday(token, companyId)
                .onStart { callbackStart.value = Unit }
                .onCompletion {}
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    isCloseToday.value = it
                }
        }
    }


    private fun checkLikeCompany(token: String) {
        viewModelScope.launch {
            repo.checkFollowCompany(token, companyDetail.value.id ?: 0, AppConstants.LIKE)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    isLike.value = it
                }
        }
    }

    fun likeOrNot(token: String) {
        if (isLike.value) unlike(token) else like(token)
    }

    private fun like(token: String) {
        viewModelScope.launch {
            repo.followCompany(token, companyDetail.value.id ?: 0, AppConstants.LIKE)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 201) {
                        isLike.value = true
                        companyDetail.value.likedCount = (companyDetail.value.likedCount ?: 0) + 1
                    }
                }
        }
    }

    private fun unlike(token: String) {
        viewModelScope.launch {
            repo.unfollowCompany(token, companyDetail.value.id ?: 0, AppConstants.LIKE)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        isLike.value = false
                        companyDetail.value.likedCount = (companyDetail.value.likedCount ?: 0) - 1
                    }
                }
        }
    }

    //Recommend
    private fun checkRecommendCompany(token: String) {
        viewModelScope.launch {
            repo.checkFollowCompany(token, companyDetail.value.id ?: 0, AppConstants.RECOMMEND)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    isRecommend.value = it
                }
        }
    }

    fun recommendOrNot(token: String) {
        if (isRecommend.value) unrecommend(token) else recommend(token)
    }

    private fun recommend(token: String) {
        viewModelScope.launch {
            repo.followCompany(token, companyDetail.value.id ?: 0, AppConstants.RECOMMEND)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 201) {
                        isRecommend.value = true
                        companyDetail.value.numberRecommend =
                            (companyDetail.value.numberRecommend ?: 0) + 1
                    }
                }
        }
    }

    private fun unrecommend(token: String) {
        viewModelScope.launch {
            repo.unfollowCompany(token, companyDetail.value.id ?: 0, AppConstants.RECOMMEND)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        isRecommend.value = false
                        companyDetail.value.numberRecommend =
                            (companyDetail.value.numberRecommend ?: 0) - 1
                    }
                }
        }
    }
}