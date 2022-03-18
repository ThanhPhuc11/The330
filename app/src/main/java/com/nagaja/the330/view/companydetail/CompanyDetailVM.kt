package com.nagaja.the330.view.companydetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CompanyDetailVM(
    private val repo: CompanyDetailRepo
) : BaseViewModel() {
    var companyDetail = mutableStateOf(CompanyModel())
    var isFollowing = mutableStateOf(false)


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
                    checkFollowCompany(token)
                }
        }
    }


    private fun checkFollowCompany(token: String) {
        viewModelScope.launch {
            repo.checkFollowCompany(token, companyDetail.value.id ?: 0)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    isFollowing.value = it
                }
        }
    }

    fun followOrNot(token: String) {
        if (isFollowing.value) unfollow(token) else follow(token)
    }

    private fun follow(token: String) {
        viewModelScope.launch {
            repo.followCompany(token, companyDetail.value.id ?: 0)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 201) {
                        isFollowing.value = true
                        companyDetail.value.likedCount = (companyDetail.value.likedCount ?: 0) + 1
                    }
                }
        }
    }

    private fun unfollow(token: String) {
        viewModelScope.launch {
            repo.unfollowCompany(token, companyDetail.value.id ?: 0)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        isFollowing.value = false
                        companyDetail.value.likedCount = (companyDetail.value.likedCount ?: 0) - 1
                    }
                }
        }
    }
}