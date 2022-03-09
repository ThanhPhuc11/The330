package com.nagaja.the330.view.reservationregis

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.TimeReservation
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.CalendarUI
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage

class ReservationRegisFragment : BaseFragment() {
    private var onClickChoose: ((Int) -> Unit)? = null

    companion object {
        fun newInstance() = ReservationRegisFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
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
                        imageModel = "",
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
                        "ten cty",
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
                        TextFieldCustom(hint = "예약자명을 입력해주세요.", modifier = Modifier.weight(2.5f))
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: SDT
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("전화번호", style = text14_222, modifier = Modifier.weight(1f))
                        TextFieldCustom(hint = "sdt", modifier = Modifier.weight(2.5f))
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Number of people
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("인원", style = text14_222, modifier = Modifier.weight(1f))
                        TextFieldCustom(hint = "예약 인원을 입력해주세요.", modifier = Modifier.weight(2.5f))
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Date
                    val showDate = remember { mutableStateOf(false) }
                    val stateDate = remember { mutableStateOf("예약일을 선택해주세요.") }
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
                            showDate.value = false
                            stateDate.value = date
                        }
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    //TODO: Time
                    val showTime = remember { mutableStateOf(false) }
                    val stateTime = remember { mutableStateOf("예약 시간을 선택해주세요.") }
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
                        ReservationTime { time ->
                            stateTime.value = time
                            showTime.value = false
                        }
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)

                    Text("요청 사항", style = text14_222, modifier = Modifier.padding(vertical = 12.dp))

                    //TODO: Body
                    val stateEdtBody = remember { mutableStateOf(TextFieldValue("")) }
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
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ColorUtils.blue_2177E4),
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
    private fun ReservationTime(onClick: ((String) -> Unit)? = null) {
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
                    onClick?.invoke(listTime[index].time)
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
}