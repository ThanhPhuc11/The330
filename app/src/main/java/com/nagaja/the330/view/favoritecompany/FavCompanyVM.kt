package com.nagaja.the330.view.favoritecompany

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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
                        listCompany.forEachIndexed { index, obj ->
                            if (obj.target?.id == targetId) {
                                val newObj = listCompany[index].apply { isFollow = true }
                                updateItem(index, newObj, listCompany)
                                return@forEachIndexed
                            }
                        }
                    }
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
                        listCompany.forEachIndexed { index, obj ->
                            if (obj.target?.id == targetId) {
                                val newObj = listCompany[index].apply { isFollow = false }
                                updateItem(index, newObj, listCompany)
                                return@forEachIndexed
                            }
                        }
                    }
                }
        }
    }

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}