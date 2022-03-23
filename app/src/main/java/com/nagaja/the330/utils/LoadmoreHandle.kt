package com.nagaja.the330.utils

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect

@Composable
fun LoadmoreHandler(
    listState: LazyListState,
    onLoadMore: (Int) -> Unit
) {
    var currentPage = 0
    var newTotalItem = 0
    var oldTotalItem = 0
    var loading = true
    val onScroll = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            newTotalItem = layoutInfo.totalItemsCount
            (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
        }
    }

    LaunchedEffect(onScroll) {
        snapshotFlow { onScroll.value }
            .collect {
                Log.e("OnScroll", "last: $it and total $newTotalItem")
                if (newTotalItem < oldTotalItem) {
                    currentPage = -1
                    oldTotalItem = newTotalItem
//                    if (newTotalItem == 0) {
//                        loading = true
//                    }
                }
                if (loading && newTotalItem > oldTotalItem) {
                    loading = false
                    oldTotalItem = newTotalItem
                }
                if (!loading && (it + 2 > newTotalItem)) {
                    currentPage++
                    onLoadMore(currentPage)
                    loading = true
                }
            }
    }
}

@Composable
fun LoadmoreMessHandler(
    listState: LazyListState,
    onLoadMore: (Int) -> Unit
) {
    var currentPage = 0
    var newTotalItem = 0
    var oldTotalItem = 0
    var loading = true
    val onScroll = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            newTotalItem = layoutInfo.totalItemsCount
            (layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0)
        }
    }

    LaunchedEffect(onScroll) {
        snapshotFlow { onScroll.value }
            .collect {
                Log.e("OnScroll", "first: $it and total $newTotalItem")
                if (newTotalItem < oldTotalItem) {
                    currentPage = -1
                    oldTotalItem = newTotalItem
                }
                if (loading && newTotalItem > oldTotalItem) {
                    loading = false
                    oldTotalItem = newTotalItem
                }
                if (!loading && (it == 0)) {
                    currentPage++
                    onLoadMore(currentPage)
                    loading = true
                }
            }
    }
}