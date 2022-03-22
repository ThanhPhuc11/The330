package com.nagaja.the330.view.regularcustomer

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CompanyFavoriteModel
import com.nagaja.the330.utils.AppConstants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegularVM(
    private val repo: RegularRepo
) : BaseViewModel() {
    var sort = ""
    var totalLikeMe = 0
    var totalMyLike = 0
    val listLikeMe = mutableStateListOf<CompanyFavoriteModel>()
    val listMyLike = mutableStateListOf<CompanyFavoriteModel>()

    fun getLikeMe(token: String, page: Int) {
        viewModelScope.launch {
            repo.getLikeMe(token, page, 10, sort = sort, AppConstants.LIKE)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (page == 0) {
                        listLikeMe.clear()
                        totalLikeMe = it.totalElements ?: 0
                    }
                    it.content?.let { it1 -> listLikeMe.addAll(it1) }
                }
        }
    }

    fun getMyLike(token: String, page: Int) {
        viewModelScope.launch {
            repo.getMyLike(token, page, 10, sort = sort, AppConstants.LIKE)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    if (page == 0) {
                        listMyLike.clear()
                        totalMyLike = it.totalElements ?: 0
                    }
                    it.content?.let { it1 -> listMyLike.addAll(it1) }
                }
        }
    }

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}