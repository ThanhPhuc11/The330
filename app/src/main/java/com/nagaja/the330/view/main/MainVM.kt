package com.nagaja.the330.view.main

import androidx.compose.runtime.mutableStateListOf
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseViewModel

class MainVM: BaseViewModel() {
    val listTab = mutableStateListOf(
        NavigationItem2("home", R.drawable.ic_home_tab, R.string.home_tab),
        NavigationItem2("reservation", R.drawable.ic_reservation_tab, R.string.reservation_tab),
        NavigationItem2("chat", R.drawable.ic_chat_tab, R.string.chat_tab),
        NavigationItem2("mypage", R.drawable.ic_my_page_tab, R.string.mypage_tab)
    )
}