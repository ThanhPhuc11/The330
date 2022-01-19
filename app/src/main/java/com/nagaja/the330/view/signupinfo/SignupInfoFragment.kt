package com.nagaja.the330.view.signupinfo

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.*

class SignupInfoFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = SignupInfoFragment()
    }

    @Composable
    override fun SetupViewModel() {
//        TODO("Not yet implemented")
    }

    @Preview
    @Composable
    override fun UIData() {
        DisposableEffect(key1 = Unit, effect = {
            Log.e("PHUC", "onStart")
            onDispose {
                Log.e("PHUC", "onStop")
            }
        })
        val checkedAll = remember { mutableStateOf(false) }
        val check1 = remember { mutableStateOf(false) }
        val check2 = remember { mutableStateOf(false) }
        val check3 = remember { mutableStateOf(false) }
        val check4 = remember { mutableStateOf(false) }
        val check5 = remember { mutableStateOf(false) }
        val check6 = remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current
        LaunchedEffect(
            check1.value,
            check2.value,
            check3.value,
            check4.value,
            check5.value,
            check6.value
        ) {
            checkedAll.value =
                check1.value && check2.value && check3.value && check4.value && check5.value && check6.value
        }
        LayoutTheme330 {
            Header(title = "", clickBack = { Log.e("HAHA", "Back!") })
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(
                        state = rememberScrollState()
                    )
            ) {
                Text(
                    stringResource(R.string.signup),
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
                Text(
                    stringResource(R.string.enter_member_infomation),
                    fontSize = 16.sp,
                    color = ColorUtils.gray_9F9F9F,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                Text(
                    stringResource(R.string.name),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                )
                TextFieldCustom(
                    hint = stringResource(R.string.input_your_name_please),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    stringResource(R.string.user_id),
                    fontWeight = FontWeight.Black,
                    style = text14_222,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    stringResource(R.string.guide_input_id),
                    fontSize = 10.sp,
                    color = ColorUtils.gray_222222,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.please_enter_id),
                    modifier = Modifier.padding(top = 4.dp),
                    maxLength = 15
                )

                Text(
                    stringResource(R.string.password),
                    fontWeight = FontWeight.Black,
                    style = text14_222,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    stringResource(R.string.guide_input_password),
                    fontSize = 10.sp,
                    color = ColorUtils.gray_222222,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.hint_input_password),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    stringResource(R.string.confirm_password),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 20.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.hint_input_confirm_password),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    stringResource(R.string.phone_label),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Box(
                        Modifier
                            .padding(end = 4.dp)
                            .size(44.dp)
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    TextFieldCustom(
                        hint = stringResource(R.string.hint_input_phone),
                        modifier = Modifier.weight(1f)
                    )
                }

                val enable = remember { mutableStateOf(false) }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = if (enable.value) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1
                    )
                ) {
                    Text(
                        stringResource(R.string.authen_request),
                        fontSize = 14.sp,
                        color = ColorUtils.gray_BEBEBE
                    )
                }

                Text(
                    stringResource(R.string.cer_number_input),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 24.dp)
                )
                TextFieldCustom(
                    hint = stringResource(R.string.hint_input_otp),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = if (enable.value) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1
                    )
                ) {
                    Text(
                        stringResource(R.string.certification),
                        fontSize = 14.sp,
                        color = ColorUtils.gray_BEBEBE
                    )
                }

                Text(
                    stringResource(R.string.address),
                    style = text14_222,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .weight(2f)
                            .height(44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "대한민국",
                            style = text14_222
                        )
                    }
                    TextFieldCustom(
                        hint = stringResource(R.string.hint_input_address),
                        modifier = Modifier.weight(4f)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_mark),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                TextFieldCustom(
                    hint = stringResource(R.string.hint_input_address),
                    modifier = Modifier
                        .weight(4f)
                        .padding(top = 8.dp)
                )

                //term
                Text(
                    stringResource(R.string.label_agree_term_to_use),
                    fontSize = 16.sp,
                    color = ColorUtils.gray_222222,
                    modifier = Modifier.padding(top = 40.dp, bottom = 17.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .padding(top = 20.dp)
                ) {
                    Checkbox(
                        checked = checkedAll.value,
                        onCheckedChange = {
                            checkedAll.value = !checkedAll.value
                            if (checkedAll.value) {
                                check1.value = true
                                check2.value = true
                                check3.value = true
                                check4.value = true
                                check5.value = true
                                check6.value = true
                            } else {
                                check1.value = false
                                check2.value = false
                                check3.value = false
                                check4.value = false
                                check5.value = false
                                check6.value = false
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ColorUtils.blue_2177E4,
//                        checkmarkColor = ColorUtils.white_FFFFFF
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        stringResource(R.string.agree_all_term),
                        fontSize = 14.sp,
                        color = ColorUtils.gray_222222,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .padding(start = 10.5.dp)
                            .weight(1f)
                    )
                }

                Box(
                    Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorUtils.gray_F5F5F5)
                )

                val collapsed1 = remember { mutableStateOf(true) }
                val collapsed2 = remember { mutableStateOf(true) }
                val collapsed3 = remember { mutableStateOf(true) }
                val collapsed4 = remember { mutableStateOf(true) }
                val collapsed5 = remember { mutableStateOf(true) }
                val collapsed6 = remember { mutableStateOf(true) }
                CheckPolicy(text = "[필수] 이용약관 동의", check1, collapsed1)
                AnimatedVisibility(visible = !collapsed1.value) {
                    ContentPolicy(content = "content1")
                }

                CheckPolicy(text = "[필수] 개인정보 이용 수집 방침", check2, collapsed2)
                AnimatedVisibility(visible = !collapsed2.value) {
                    ContentPolicy(content = "content2")
                }

                CheckPolicy(text = "[필수] 개인정보 제 3자 제공 동의", check3, collapsed3)
                AnimatedVisibility(visible = !collapsed3.value) {
                    ContentPolicy(content = "content3")
                }

                CheckPolicy(text = "[필수] 위치정보 동의 약관", check4, collapsed4)
                AnimatedVisibility(visible = !collapsed4.value) {
                    ContentPolicy(content = "content4")
                }

                CheckPolicy(text = "[선택] 이벤트 및 혜택 알림 수신 동의", check5, collapsed5)
                AnimatedVisibility(visible = !collapsed5.value) {
                    ContentPolicy(content = "content5")
                }

                CheckPolicy(text = "[선택] 서비스 알림 수신 동의", check6, collapsed6)
                AnimatedVisibility(visible = !collapsed6.value) {
                    ContentPolicy(content = "content6")
                }
            }
        }
    }

    @Composable
    fun CheckPolicy(
        text: String,
        checked: MutableState<Boolean>,
        collapsed: MutableState<Boolean>
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .noRippleClickable {
                    collapsed.value = !collapsed.value
                }
        ) {
            Checkbox(
                checked = checked.value,
                onCheckedChange = {
                    checked.value = !checked.value
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorUtils.black_000000,
                ),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text,
                fontSize = 14.sp,
                color = ColorUtils.gray_626262,
                modifier = Modifier
                    .padding(start = 10.5.dp)
                    .weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = "",
                Modifier
                    .rotate(if (collapsed.value) 0f else 180f)
            )
        }
    }

    @Composable
    fun ContentPolicy(content: String) {
        Column(
            Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .background(ColorUtils.gray_F5F5F5)
                .fillMaxWidth()
                .height(200.dp)
                .padding(9.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            Text(
                content,
                color = ColorUtils.gray_767676,
                fontSize = 12.sp
            )
        }
    }
}