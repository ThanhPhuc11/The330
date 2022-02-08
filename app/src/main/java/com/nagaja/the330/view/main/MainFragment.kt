package com.nagaja.the330.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.home.HomeScreen
import com.nagaja.the330.view.mypage.MyPageScreen

class MainFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    @Composable
    override fun SetupViewModel() {
    }

    @Preview
    @Composable
    override fun UIData() {
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
                Page2()
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
        HomeScreen()
    }

    @Composable
    fun MypageTab() {
        MyPageScreen()
    }

    @Composable
    fun Page2() {
        Column(
            Modifier
                .fillMaxSize()
                .background(ColorUtils.pink_FF1E54)
        ) {

        }
    }
}