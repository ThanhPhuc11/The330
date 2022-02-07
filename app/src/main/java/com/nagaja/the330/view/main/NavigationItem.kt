package com.nagaja.the330.view.main

import com.nagaja.the330.R

sealed class NavigationItem(var route: String, var icon: Int, var title: Int) {
    object Home : NavigationItem("home", R.drawable.ic_home_tab, R.string.home_tab)
    object Reservation : NavigationItem("reservation", R.drawable.ic_reservation_tab, R.string.reservation_tab)
    object Chat : NavigationItem("chat", R.drawable.ic_chat_tab, R.string.chat_tab)
    object MyPage : NavigationItem("mypage", R.drawable.ic_my_page_tab, R.string.mypage_tab)
}