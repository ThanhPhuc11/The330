package com.nagaja.the330.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.ui.theme.The330Theme
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330

class MainFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    @Composable
    override fun SetupViewModel() {
        TODO("Not yet implemented")
    }

    @Preview
    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(ColorUtils.white_FFFFFF)
            ) {

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(ColorUtils.gray_D9D9D9)
            )
            BottomNavigationBar()
        }
    }

    @Preview
    @Composable
    fun BottomNavigationBar() {
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
                    selected = false,
                    onClick = {
                        /* Add code later */
                    }
                )
            }
        }
    }
}