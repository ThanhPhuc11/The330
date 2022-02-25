package com.nagaja.the330.view.secondhand

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

class SecondHandVM(
    private val repo: SecondHandRepo
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

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}