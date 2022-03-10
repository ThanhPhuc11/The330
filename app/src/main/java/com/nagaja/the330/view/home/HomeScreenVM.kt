package com.nagaja.the330.view.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.CategoryModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeScreenVM(
    private val repo: HomeScreenRepo
) : BaseViewModel() {

    //    val listCategoryState = mutableStateListOf<CategoryModel>()
    val listCategoryState = mutableStateOf(mutableListOf<CategoryModel>())
    fun getCategory(token: String, group: String?) {
        viewModelScope.launch {
            repo.getCategory(token, group)
                .onStart { }
                .onCompletion { }
                .catch { }
                .collect {
                    it.content?.let { it1 -> listCategoryState.value = it1 }
//                    it.content?.let { it1 -> listCategoryState.addAll(it1) }
                }
        }
    }
}