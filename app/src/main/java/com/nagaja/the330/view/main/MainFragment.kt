package com.nagaja.the330.view.main

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nagaja.the330.MainActivity
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.chatlist.ChatListScreen
import com.nagaja.the330.view.chatlist.onClickUpdateList
import com.nagaja.the330.view.home.HomeScreen
import com.nagaja.the330.view.mypage.MyPageScreen
import com.nagaja.the330.view.mypagecompany.MyPageCompanyScreen
import com.nagaja.the330.view.reservation.ReservationScreen
import com.nagaja.the330.view.reservationcompany.ReservationCompanyScreen

class MainFragment : BaseFragment() {
    private lateinit var viewModel: MainVM
    private var userDetail = mutableStateOf(UserDetail())
    private var isFirst = true
    private lateinit var database: DatabaseReference

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[MainVM::class.java]
        viewController = (activity as MainActivity).viewController
        database = Firebase.database.reference
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
        val items = viewModel.listTab

        BottomNavigation(
            backgroundColor = ColorUtils.white_FFFFFF,
            contentColor = ColorUtils.gray_BEBEBE,
            modifier = Modifier.wrapContentHeight()
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier.padding(top = 10.dp),
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.isShow)
                                    Badge(
                                        backgroundColor = ColorUtils.pink_FF1E54,
                                        contentColor = ColorUtils.white_FFFFFF
                                    ) { Text("N") }
                            }
                        ) {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = stringResource(item.title)
                            )
                        }
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

                            if (item.route == "chat") {
                                viewModel.listTab[2] = viewModel.listTab[2].copy(isShow = false)
                            }
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
                ChatTab()
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
    fun ChatTab() {
        accessToken?.let { ChatListScreen(it, viewController, userDetail.value) }
    }

    @Composable
    fun MypageTab() {
        accessToken?.let {
            if (userDetail.value.userType == AppConstants.COMPANY) {
                MyPageCompanyScreen(it, viewController)
            } else {
                MyPageScreen(it, viewController)
            }
        }
    }

    private fun getUserDetailFromDataStore(context: Context) {
//        CoroutineScope(Dispatchers.IO).launch {
//            context.dataStore.data.map { get ->
//                get[DataStorePref.USER_DETAIL] ?: ""
//            }.collect {
//                val userDetail = Gson().fromJson(it, UserDetail::class.java)
//                userDetail?.let {
//                    this@MainFragment.userDetail.value = userDetail
//                }
//            }
//        }
        userDetail.value = userDetailBase!!
        listenRealtime()
    }

    private fun listenRealtime() {
        database.child("users")
            .child(userDetail.value.id.toString()).child("messageId")
            .addValueEventListener(eventListenerRealtime)
    }

    private val eventListenerRealtime = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.e("FIREBASE_LASTMESS_FORME", snapshot.value.toString())
            if (isFirst) {
                isFirst = false
            } else {
                viewModel.listTab[2] = viewModel.listTab[2].copy(isShow = true)
            }
            onClickUpdateList?.invoke()
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
}