package com.nagaja.the330.view.mypage

import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.google.gson.Gson
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.edit_profile.EditProfileFragment
import com.nagaja.the330.view.favoritecompany.FavCompanyFragment
import com.nagaja.the330.view.favoritecompany.FavCompanyVM
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

private lateinit var mypageVM: MyPageScreenVM

@Composable
fun MyPageScreen(accessToken: String, viewController: ViewController?) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val clickFavorite: (() -> Unit) = {
        viewController?.pushFragment(
            ScreenId.SCREEN_FAV_COMPANY,
            FavCompanyFragment.newInstance()
        )
    }
    val viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
    mypageVM = ViewModelProvider(
        viewModelStoreOwner,
        ViewModelFactory(
            RetrofitBuilder.getInstance(context)
                ?.create(ApiService::class.java)!!
        )
    )[MyPageScreenVM::class.java]

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    getUserDetailFromDataStore(context)
                    mypageVM.getUserDetails(accessToken)
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
    LayoutTheme330 {
        Header(title = stringResource(R.string.mypage_tab), clickBack = null)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(state = rememberScrollState())
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(96.dp)
                        .height(32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorUtils.gray_222222)
                ) {
                    Text(
                        stringResource(R.string.apply_company_member),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .background(ColorUtils.gray_F5F5F5)
                    .fillMaxWidth()
                    .height(8.dp)
            )
            MyInfo(viewController)
            Spacer(
                modifier = Modifier
                    .background(ColorUtils.gray_F5F5F5)
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Column(Modifier.background(ColorUtils.gray_E1E1E1)) {
                MypageOptionItem(
                    R.drawable.ic_favorite,
                    stringResource(R.string.option_favorite_store_list),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_consultation_opt,
                    stringResource(R.string.option_consultation_history),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_reservation_opt,
                    stringResource(R.string.option_reservation_status),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_usage_opt,
                    stringResource(R.string.option_usage_list),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_secondhand_purchase_opt,
                    stringResource(R.string.option_secondhand_purchase_list),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_job_opt,
                    stringResource(R.string.option_recruitment_list),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_report_opt,
                    stringResource(R.string.option_report_list),
                    clickFavorite
                )
                MypageOptionItem(
                    R.drawable.ic_other_setting_opt,
                    stringResource(R.string.option_other_setting),
                    clickFavorite
                )
            }
        }
    }
}

@Composable
private fun MyInfo(viewController: ViewController?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(end = 10.dp)
        ) {
            val userDetail = mypageVM.userDetailState.value
            Text("${stringResource(R.string.user_id)}: ${userDetail?.name}", style = text14_222)
            Text("${stringResource(R.string.name)}: ${userDetail?.realName}", style = text14_222)
            Text(
                "${stringResource(R.string.phone_number)}: ${userDetail?.phone}",
                style = text14_222
            )
            Row() {
                Text("${stringResource(R.string.service_use_address)}: ", style = text14_222)
                Text("${userDetail?.address}", style = text14_222, textAlign = TextAlign.Start)
            }
        }
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(28.dp)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_222222,
                    shape = RoundedCornerShape(99.dp)
                )
                .noRippleClickable {
                    viewController?.pushFragment(
                        ScreenId.SCREEN_EDIT_PROFILE,
                        EditProfileFragment.newInstance()
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.edit), color = ColorUtils.gray_222222, fontSize = 12.sp)
        }
    }
}

@Composable
private fun MypageOptionItem(icon: Int, label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .padding(bottom = 1.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(ColorUtils.white_FFFFFF)
            .padding(horizontal = 16.dp)
            .noRippleClickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(icon), contentDescription = null)
        Text(label, style = text14_222, modifier = Modifier.padding(start = 16.dp))
    }
}

private fun getUserDetailFromDataStore(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.data.map { get ->
            get[DataStorePref.USER_DETAIL] ?: ""
        }.collect {
            val userDetail = Gson().fromJson(it, UserDetail::class.java)
            userDetail?.let { mypageVM.userDetailState.value = userDetail }
        }
    }
}