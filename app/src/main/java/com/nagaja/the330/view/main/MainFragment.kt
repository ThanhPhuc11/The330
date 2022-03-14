package com.nagaja.the330.view.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.home.HomeScreen
import com.nagaja.the330.view.mypage.MyPageScreen
import com.nagaja.the330.view.reservation.ReservationScreen
import com.nagaja.the330.view.reservationcompany.ReservationCompanyScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainFragment : BaseFragment() {
    private var userDetail = mutableStateOf(UserDetail())

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(owner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        getUserDetailFromDataStore(requireContext())
                    }
                    Lifecycle.Event.ON_STOP -> {

                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        val navController = rememberNavController()
        LayoutTheme330 {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(ColorUtils.white_FFFFFF)
            ) {
                Navigation(navController)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(ColorUtils.gray_D9D9D9)
            )
            BottomNavigationBar(navController)

        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavController) {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Reservation,
            NavigationItem.Chat,
            NavigationItem.MyPage
        )

        BottomNavigation(
            backgroundColor = ColorUtils.white_FFFFFF,
            contentColor = ColorUtils.gray_BEBEBE,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = stringResource(item.title)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.title),
                            fontSize = 10.sp,
                        )
                    },
                    selectedContentColor = ColorUtils.blue_2177E4,
                    unselectedContentColor = ColorUtils.gray_BEBEBE,
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        /* Add code later */
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                HomeTab()
            }
            composable(NavigationItem.Reservation.route) {
                ReservationTab()
            }
            composable(NavigationItem.Chat.route) {
                Page2()
            }
            composable(NavigationItem.MyPage.route) {
                MypageTab()
            }
        }
    }

    @Composable
    fun HomeTab() {
        accessToken?.let { HomeScreen(it, viewController) }
    }

    @Composable
    fun ReservationTab() {
        accessToken?.let {
            if (userDetail.value.userType == AppConstants.COMPANY) {
                ReservationCompanyScreen(it, viewController)
            } else {
                ReservationScreen(it, viewController)
            }
        }
    }

    @Composable
    fun MypageTab() {
        accessToken?.let { MyPageScreen(it, viewController) }
    }

    @Composable
    fun Page2() {
        Column(
            Modifier
                .fillMaxSize()
                .background(ColorUtils.white_FFFFFF)
        ) {

        }
    }

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { this@MainFragment.userDetail.value = userDetail }
            }
        }
    }
}