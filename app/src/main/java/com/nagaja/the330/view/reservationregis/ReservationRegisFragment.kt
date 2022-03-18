package com.nagaja.the330.view.reservationregis

import android.content.Context
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.data.dataStore
import com.nagaja.the330.model.TimeReservation
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReservationRegisFragment : BaseFragment() {
    private lateinit var viewModel: ReservationRegisVM
    private var onClickChoose: ((Int) -> Unit)? = null

    companion object {
        fun newInstance(companyId: Int) = ReservationRegisFragment().apply {
            arguments = Bundle().apply {
                putInt(AppConstants.EXTRA_KEY1, companyId)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ReservationRegisVM::class.java]
        viewController = (activity as MainActivity).viewController

        viewModel.callbackStart.observe(viewLifecycleOwner) {
            showLoading()
        }
        viewModel.callbackSuccess.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.callbackFail.observe(viewLifecycleOwner) {
            hideLoading()
        }
        viewModel.showMessCallback.observe(viewLifecycleOwner) {
            showMess(it)
        }
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        getUserDetailFromDataStore(requireContext())
                        viewModel.id = requireArguments().getInt(AppConstants.EXTRA_KEY1)
                        viewModel.getCompanyDetail(accessToken!!)
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        val showDate = remember { mutableStateOf(false) }
        val stateDate = remember { mutableStateOf("예약일을 선택해주세요.") }

        val showTime = remember { mutableStateOf(false) }
        val stateTime = remember { mutableStateOf("예약 시간을 선택해주세요.") }

        val stateDialogSuccess = remember {
            mutableStateOf(false)
        }
        if (stateDialogSuccess.value)
            Dialog2Button(
                state = stateDialogSuccess,
                title = "예약 완료",
                content = "${stateDate.value} ${stateTime.value} \n" +
                        "${viewModel.stateEdtNumber.value.text}명 \n" +
                        "예약 내역을 확인하시겠습니까?",
                leftText = "아니오",
                rightText = "예",
                onClick = {
                    if (it) {
                        viewController?.popToFragment(ScreenId.SCREEN_MAIN)
//                    accessToken?.let { viewModel.makeReservation(accessToken!!) }
                    }
                }
            )
        LaunchedEffect(viewModel.callbackMakeReservationSuccess.value) {
            if (viewModel.callbackMakeReservationSuccess.value) {
                stateDialogSuccess.value = true
            }
        }

        LayoutTheme330 {
            Header(stringResource(R.string.make_reservation)) {
                viewController?.popFragment()
            }
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        imageModel = "${BuildConfig.BASE_S3}${
                            viewModel.companyDetail.value.images?.getOrNull(
                                0
                            )?.url
                        }",
                        contentDescription = "",
                        placeHolder = painterResource(R.drawable.ic_default_nagaja),
                        error = painterResource(R.drawable.ic_default_nagaja),
                        requestOptions = {
                            RequestOptions()
                                .override(360, 360)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                        },
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        "${viewModel.companyDetail.value.name?.getOrNull(0)?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 13.dp)
                    )
                }
                Divider(
                    color = ColorUtils.gray_F5F5F5,
                    thickness = 8.dp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        "예약정보",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 19.dp)
                    )
                    Divider(color = ColorUtils.gray_E1E1E1)
                    //TODO: Name
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("예약자명", style = text14_222, modifier = Modifier.weight(1f))
                        TextFieldCustom(
                            hint = "예약자명을 입력해주세요.",
                            modifier = Modifier.weight(2.5f),
                            textStateId = viewModel.stateEdtBooker
                        )
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: SDT
                    LaunchedEffect(viewModel.userDetail.value) {
                        viewModel.stateEdtPhoneNumber.value =
                            TextFieldValue(viewModel.userDetail.value.phone ?: "")
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("전화번호", style = text14_222, modifier = Modifier.weight(1f))
                        TextFieldCustom(
                            hint = "",
                            modifier = Modifier.weight(2.5f),
                            textStateId = viewModel.stateEdtPhoneNumber
                        )
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Number of people
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("인원", style = text14_222, modifier = Modifier.weight(1f))
                        TextFieldCustom(
                            hint = "예약 인원을 입력해주세요.",
                            modifier = Modifier.weight(2.5f),
                            inputType = KeyboardType.Number,
                            textStateId = viewModel.stateEdtNumber
                        )
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Date
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .noRippleClickable {
                                showDate.value = !showDate.value
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("날짜", style = text14_222, modifier = Modifier.weight(1f))
                        Text(
                            text = stateDate.value,
                            color = ColorUtils.gray_BEBEBE,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(2.5f)
                                .padding(start = 8.dp)
                        )
                    }
                    AnimatedVisibility(visible = showDate.value) {
                        CalendarUI { date ->
                            stateDate.value = date
                            viewModel.date = date
                            showDate.value = false
                        }
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Time
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .noRippleClickable {
                                showTime.value = !showTime.value
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("시간", style = text14_222, modifier = Modifier.weight(1f))
                        Text(
                            text = stateTime.value,
                            color = ColorUtils.gray_BEBEBE,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(2.5f)
                                .padding(start = 8.dp)
                        )
                    }
                    AnimatedVisibility(visible = showTime.value) {
                        ReservationTime { index, time ->
                            stateTime.value = time
                            viewModel.time = index
                            showTime.value = false
                        }
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    Text("요청 사항", style = text14_222, modifier = Modifier.padding(vertical = 12.dp))

                    //TODO: Body
                    val stateEdtBody = viewModel.stateEdtRequestNote
                    Box(
                        modifier = Modifier
                            .padding(bottom = 40.dp)
                            .height(154.dp)
                            .background(ColorUtils.white_FFFFFF)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        BasicTextField(
                            value = stateEdtBody.value,
                            onValueChange = {
                                if (it.text.length <= 5000) stateEdtBody.value = it
                            },
                            Modifier
                                .fillMaxWidth(),
                            textStyle = TextStyle(
                                color = ColorUtils.black_000000
                            ),
                            decorationBox = { innerTextField ->
                                Row {
                                    if (stateEdtBody.value.text.isEmpty()) {
                                        Text(
                                            text = stringResource(R.string.please_enter_your_request),
                                            color = ColorUtils.gray_BEBEBE,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }
            val stateDialogConfirm = remember {
                mutableStateOf(false)
            }
            if (stateDialogConfirm.value) {
                Dialog2Button(
                    state = stateDialogConfirm,
                    title = "예약 신청",
                    content = "예약자명 : ${viewModel.stateEdtBooker.value.text}\n" +
                            "전화번호: ${viewModel.stateEdtPhoneNumber.value.text}\n" +
                            "${stateDate.value} ${stateTime.value} \n" +
                            "${viewModel.stateEdtNumber.value.text}명 예약을 진행하시겠습니까?",
                    leftText = "취소하기",
                    rightText = "예약하기",
                    onClick = {
                        if (it) {
                            accessToken?.let { viewModel.makeReservation(accessToken!!) }
                        }
                    }
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {
                        stateDialogConfirm.value = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.make_reservation),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @Composable
    fun TextFieldCustom(
        modifier: Modifier = Modifier,
        textStateId: MutableState<TextFieldValue> = remember {
            mutableStateOf(TextFieldValue(""))
        },
        hint: String = "",
        maxLength: Int = 1000,
        inputType: KeyboardType = KeyboardType.Text,
        isPw: Boolean = false,
        focusable: Boolean = true,
    ) {
        Box(
            modifier = modifier
                .background(ColorUtils.white_FFFFFF)
                .height(46.dp)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textStateId.value,
                onValueChange = {
                    if (it.text.length <= maxLength) textStateId.value = it
                },
                Modifier
                    .fillMaxWidth(),
                singleLine = true,
                enabled = focusable,
//            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                visualTransformation = if (isPw) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = inputType),
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (textStateId.value.text.isEmpty()) {
                            Text(
                                text = hint,
                                color = ColorUtils.gray_BEBEBE,
                                fontSize = 14.sp
                            )
                        }
                    }
                    innerTextField()
                }
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Preview
    @Composable
    private fun ReservationTime(onClick: ((Int, String) -> Unit)? = null) {
        Column(
            Modifier
                .padding(top = 4.dp, bottom = 12.dp)
        ) {
            Text(
                "※예약은 {mm}분 단위입니다.",
                color = ColorUtils.gray_222222,
                fontSize = 12.sp
            )

            val listTime =
                remember {
                    mutableStateListOf(
                        TimeReservation("00:00"), TimeReservation("00:30"),
                        TimeReservation("01:00"), TimeReservation("01:30"),
                        TimeReservation("02:00"), TimeReservation("02:30"),
                        TimeReservation("03:00"), TimeReservation("03:30"),
                        TimeReservation("04:00"), TimeReservation("04:30"),
                        TimeReservation("05:00"), TimeReservation("05:30"),
                        TimeReservation("06:00"), TimeReservation("06:30"),
                        TimeReservation("07:00"), TimeReservation("07:30"),
                        TimeReservation("08:00"), TimeReservation("08:30"),
                        TimeReservation("09:00"), TimeReservation("09:30"),
                        TimeReservation("10:00"), TimeReservation("10:30"),
                        TimeReservation("11:00"), TimeReservation("11:30"),
                        TimeReservation("12:00"), TimeReservation("12:30"),
                        TimeReservation("13:00"), TimeReservation("13:30"),
                        TimeReservation("14:00"), TimeReservation("14:30"),
                        TimeReservation("15:00"), TimeReservation("15:30"),
                        TimeReservation("16:00"), TimeReservation("16:30"),
                        TimeReservation("17:00"), TimeReservation("17:30"),
                        TimeReservation("18:00"), TimeReservation("18:30"),
                        TimeReservation("19:00"), TimeReservation("19:30"),
                        TimeReservation("20:00"), TimeReservation("20:30"),
                        TimeReservation("21:00"), TimeReservation("21:30"),
                        TimeReservation("22:00"), TimeReservation("22:30"),
                        TimeReservation("23:00"), TimeReservation("23:30"),
                    )
                }

            onClickChoose = { index ->
                if (index >= 0) {
                    val temp = listTime[index].apply {
                        isSelected = !isSelected
                    }
                    listTime.removeAt(index)
                    listTime.add(index, temp)
                    onClick?.invoke(index, listTime[index].time)
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .heightIn(0.dp, 1000.dp)
                    .wrapContentHeight()
            ) {
                LazyVerticalGrid(
//                    state = rememberLazyListState(),
                    cells = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(listTime) { index, time ->
                        ItemTime(index, time)
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemTime(index: Int, time: TimeReservation, modifier: Modifier = Modifier) {
        Box(
            modifier
                .padding(top = 8.dp)
                .height(32.dp)
                .border(
                    width = 1.dp,
                    color = if (time.isSelected) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(if (time.isSelected) ColorUtils.blue_E9F1FC else ColorUtils.white_FFFFFF)
                .noRippleClickable {
                    onClickChoose?.invoke(index)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                time.time,
                color = if (time.isSelected) ColorUtils.blue_2177E4 else ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }

    private val text14_222 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
        color = ColorUtils.gray_222222,
        fontSize = 14.sp,
        textAlign = TextAlign.Start
    )

    private fun getUserDetailFromDataStore(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { get ->
                get[DataStorePref.USER_DETAIL] ?: ""
            }.collect {
                val userDetail = Gson().fromJson(it, UserDetail::class.java)
                userDetail?.let { viewModel.userDetail.value = userDetail }
            }
        }
    }
}