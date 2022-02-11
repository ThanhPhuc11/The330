package com.nagaja.the330.view.favoritecompany

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyFavoriteModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FavCompanyVM(
    private val repo: FavCompanyRepo
) : BaseViewModel() {
    val listCompany = mutableStateListOf<CompanyFavoriteModel>()

    fun getFavoriteCompany(token: String, page: Int, sort: String) {
        viewModelScope.launch {
            repo.getFavoriteCompany(token, page, 10, sort)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 -> listCompany.addAll(it1) }
                }
        }
    }

    fun followOrNot(token: String, targetId: Int, isFollowing: Boolean) {
        if (!isFollowing) {
            follow(token, targetId)
        } else {
            unfollow(token, targetId)
        }
    }

    private fun follow(token: String, targetId: Int) {
        viewModelScope.launch {
            repo.followCompany(token, targetId)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 201) {
                        //TODO: update UI to follow
                        listCompany.firstOrNull { it1 -> it1.target?.id == targetId }?.isFollow =
                            true
                    }
                    listCompany
                }
        }
    }

    private fun unfollow(token: String, targetId: Int) {
        viewModelScope.launch {
            repo.unfollowCompany(token, targetId)
                .onStart { }
                .onCompletion { }
                .catch {
                    Log.e("PHUC", "$this")
                }
                .collect {
                    if (it.raw().isSuccessful && it.raw().code == 204) {
                        //TODO: update UI to unFollow
                        listCompany.firstOrNull { it1 -> it1.target?.id == targetId }?.isFollow =
                            false
                    }
                    listCompany
                }
        }
    }
}