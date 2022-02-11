package com.nagaja.the330.view.favoritecompany

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
}