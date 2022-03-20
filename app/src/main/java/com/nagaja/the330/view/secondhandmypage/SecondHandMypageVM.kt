package com.nagaja.the330.view.secondhandmypage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.nagaja.the330.base.BaseViewModel
import com.nagaja.the330.model.SecondHandModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SecondHandMypageVM(
    private val repo: SecondHandMypageRepo
) : BaseViewModel() {
    var sort: String? = "LASTEST"
    var transactionStatus: String? = null
    val stateListSecondhand = mutableStateListOf<SecondHandModel>()

    fun getMySecondHand(token: String, page: Int) {
        viewModelScope.launch {
            repo.getMySecondHand(token, page, 20, sort, transactionStatus)
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch { handleError(it) }
                .collect {
                    callbackSuccess.value = Unit
                    if (page == 0) {
                        stateListSecondhand.clear()
                    }
                    it.content?.let { it1 ->
                        stateListSecondhand.addAll(it1)
                    }
                }
        }
    }

    private fun <T> updateItem(index: Int, newObj: T, list: MutableList<T>) {
        list.removeAt(index)
        list.add(index, newObj)
    }
}