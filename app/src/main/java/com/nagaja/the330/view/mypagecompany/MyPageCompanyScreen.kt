package com.nagaja.the330.view.mypagecompany

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
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
import com.nagaja.the330.view.*
import com.nagaja.the330.view.edit_profile.EditProfileFragment
import com.nagaja.the330.view.editcompany.EditCompanyFragment
import com.nagaja.the330.view.othersetting.OtherSettingFragment
import com.nagaja.the330.view.point.PointFragment
import com.nagaja.the330.view.recruimentcompany.RecruitmentCompanyFragment
import com.nagaja.the330.view.regularcustomer.RegularFragment
import com.nagaja.the330.view.reportmissingmypage.ReportMissingMyPageFragment
import com.nagaja.the330.view.secondhandmypage.SecondHandMypageFragment
import com.nagaja.the330.view.usagecompany.CompanyUsageFragment
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private lateinit var viewModel: MyPageCompanyScreenVM

@Composable
fun MyPageCompanyScreen(accessToken: String, viewController: ViewController?) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val clickRegular: (() -> Unit) = {
        viewController?.pushFragment(
            ScreenId.SCREEN_REGULAR_CUSTOMER_HISTORY,
            RegularFragment.newInstance()
        )
        showMessDebug("SCREEN_REGULAR_CUSTOMER_HISTORY", context)
    }

    val clickConsultation: (() -> Unit) = {
        viewController?.pushFragment(
            ScreenId.SCREEN_CONSULTATION,
            RegularFragment.newInstance()
        )
        showMessDebug("SCREEN_CONSULTATION", context)
    }

    val clickReservationHistory: (() -> Unit) = {
        showMessDebug("SCREEN_RESERVATION", context)
    }

    val clickRecruitment: (() -> Unit) = {
        viewController?.pushFragment(
            ScreenId.SCREEN_RECRUITMENT_COMPANY,
            RecruitmentCompanyFragment.newInstance()
        )
        showMessDebug("SCREEN_RECRUITMENT_COMPANY", context)
    }

    val clickPoint: () -> Unit = {
        viewController?.pushFragment(
            ScreenId.SCREEN_POINT,
            PointFragment.newInstance()
        )
        showMessDebug("SCREEN_POINT", context)
    }

    val clickUsage: () -> Unit = {
        viewController?.pushFragment(
            ScreenId.SCREEN_USAGE,
            CompanyUsageFragment.newInstance()
        )
        showMessDebug("SCREEN_USAGE", context)
    }
    val clickSecondHandPurchase: () -> Unit = {
        viewController?.pushFragment(
            ScreenId.SCREEN_SECONDHAND_PURCHARGE,
            SecondHandMypageFragment.newInstance()
        )
        showMessDebug("SCREEN_SECONDHAND_PURCHARGE", context)
    }
    val clickOtherSetting: () -> Unit = {
        viewController?.pushFragment(
            ScreenId.SCREEN_OTHER_SETTING,
            OtherSettingFragment.newInstance()
        )
        showMessDebug("SCREEN_OTHER_SETTING", context)
    }

    val clickReport: () -> Unit = {
        viewController?.pushFragment(
            ScreenId.SCREEN_REPORT_MISSING_MYPAGE,
            ReportMissingMyPageFragment.newInstance()
        )
        showMessDebug("SCREEN_REPORT_MISSING_MYPAGE", context)
    }
    val viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
    viewModel = ViewModelProvider(
        viewModelStoreOwner,
        ViewModelFactory(
            RetrofitBuilder.getInstance(context)
                ?.create(ApiService::class.java)!!
        )
    )[MyPageCompanyScreenVM::class.java]

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    getUserDetailFromDataStore(context)
                    viewModel.getUserDetails(accessToken)
                    viewModel.cbUpdateUserDataStore.observe(owner) {
                        DataStorePref(context).setUserDetail(it)
                    }
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
            val stateShowCompany = remember { mutableStateOf(true) }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        stateShowCompany.value = !stateShowCompany.value
                    },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorUtils.gray_222222)
                ) {
                    Text(
                        stringResource(if (stateShowCompany.value) R.string.view_my_info else R.string.view_company_info),
                        color = ColorUtils.white_FFFFFF,
                        fontSize = 12.sp
                    )
                }
            }
            Divider(
                color = ColorUtils.gray_F5F5F5,
                thickness = 8.dp
            )
            if (stateShowCompany.value) {
                MyInfoCompany(viewController)
            } else {
                MyInfo(viewController)
            }
            Divider(
                color = ColorUtils.gray_F5F5F5,
                thickness = 8.dp
            )
            Column(Modifier.background(ColorUtils.gray_E1E1E1)) {
                MypageOptionItem(
                    R.drawable.ic_point_opt,
                    stringResource(R.string.option_point),
                    "잔여: 000,000",
                    clickPoint
                )
                MypageOptionItem(
                    R.drawable.ic_consultation_opt,
                    stringResource(R.string.option_consultation_history),
                    "상담중: 00건",
                    clickConsultation
                )
                MypageOptionItem(
                    R.drawable.ic_usage_opt,
                    stringResource(R.string.option_usage_list),
                    "오늘: 00건",
                    clickUsage
                )
                MypageOptionItem(
                    R.drawable.ic_reservation_opt,
                    stringResource(R.string.option_reservation_status),
                    "오늘: 00건",
                    clickReservationHistory
                )
                MypageOptionItem(
                    R.drawable.ic_favorite,
                    stringResource(R.string.option_favorite_store_list),
                    "총: 00건",
                    clickRegular
                )
                MypageOptionItem(
                    R.drawable.ic_job_opt,
                    stringResource(R.string.option_recruitment_list),
                    null,
                    clickRecruitment
                )
                MypageOptionItem(
                    R.drawable.ic_secondhand_purchase_opt,
                    stringResource(R.string.option_secondhand_purchase_list),
                    null,
                    clickSecondHandPurchase
                )
                MypageOptionItem(
                    R.drawable.ic_report_opt,
                    stringResource(R.string.option_report_list),
                    null,
                    clickReport
                )
                MypageOptionItem(
                    R.drawable.ic_other_setting_opt,
                    stringResource(R.string.option_other_setting),
                    null,
                    clickOtherSetting
                )
            }
        }
    }
}

@Composable
private fun MyInfoCompany(viewController: ViewController?) {
    val userDetail = viewModel.userDetailState.value
    val companyDetail = viewModel.companyDetailState.value
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${
                    companyDetail.images?.getOrNull(
                        0
                    )?.url
                }",
                Modifier
                    .size(84.dp)
                    .clip(shape = CircleShape),
                placeHolder = painterResource(R.drawable.ic_default_avt),
                error = painterResource(R.drawable.ic_default_avt),
                circularReveal = CircularReveal(duration = 0),
            )

            Column(
                Modifier
                    .padding(start = 15.dp)
                    .weight(1f)
                    .height(84.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "업체명: ${companyDetail.name?.getOrNull(0)?.name}",
                    style = text14_222
                )
                Text(
                    "전화번호: ${companyDetail.chargePhone}",
                    style = text14_222,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Box(
                Modifier
                    .size(52.dp, 28.dp)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_222222,
                        shape = RoundedCornerShape(99.dp)
                    )
                    .noRippleClickable {
                        viewController?.pushFragment(
                            ScreenId.SCREEN_EDIT_COMPANY,
                            EditCompanyFragment.newInstance()
                        )
                    }, contentAlignment = Alignment.Center
            ) {
                Text("수정", color = ColorUtils.gray_222222, fontSize = 12.sp)
            }
        }
        Text(
            "등록된 주소: ${companyDetail.address ?: ""}",
            style = text14_222,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 20.dp)
        )
        Text(
            "등록된 기업 소개: ${companyDetail.description?.getOrNull(0)?.name}",
            style = text14_222,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 4.dp)
        )
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
            val userDetail = viewModel.userDetailState.value
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
                        EditProfileFragment
                            .newInstance()
                            .apply {
                                callbackUpdate = {
                                    accessToken?.let {
                                        viewModel.getUserDetails(it)
                                    }
                                }
                            }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.edit), color = ColorUtils.gray_222222, fontSize = 12.sp)
        }
    }
}

@Composable
private fun MypageOptionItem(
    icon: Int,
    label: String,
    infoCase: String? = null,
    onClick: () -> Unit
) {
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
        Text(
            label, style = text14_222, modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            textAlign = TextAlign.Start
        )
        AnimatedVisibility(infoCase != null) {
            Text(infoCase ?: "", style = text14_222)
        }
    }
}

private fun getUserDetailFromDataStore(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.data.map { get ->
            get[DataStorePref.USER_DETAIL] ?: ""
        }.collect {
            val userDetail = Gson().fromJson(it, UserDetail::class.java)
            userDetail?.let { viewModel.userDetailState.value = userDetail }
        }
    }
}