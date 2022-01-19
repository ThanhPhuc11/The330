package com.nagaja.the330.view.term_sns

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.noRippleClickable

class TermSNSFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = TermSNSFragment()
    }

    @Composable
    override fun SetupViewModel() {

    }

    @Preview(showBackground = true)
    @Composable
    override fun UIData() {
        val checkedAll = remember { mutableStateOf(false) }
        val check1 = remember { mutableStateOf(false) }
        val check2 = remember { mutableStateOf(false) }
        val check3 = remember { mutableStateOf(false) }

        LaunchedEffect(check1.value, check2.value, check3.value) {
            checkedAll.value = check1.value && check2.value && check3.value
        }
        Column(
            modifier = Modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxSize()
                .noRippleClickable { }

        ) {
            Header(clickBack = { Log.e("HAHA", "Back!") })
            Column(
                Modifier
//                    .fillMaxSize()
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(
//                        orientation = Orientation.Vertical,
                        state = rememberScrollState()
                    )
            ) {
                Text(
                    "이용약관",
                    color = ColorUtils.gray_222222,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 20.dp)
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
                            } else {
                                check1.value = false
                                check2.value = false
                                check3.value = false
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ColorUtils.blue_2177E4,
//                        checkmarkColor = ColorUtils.white_FFFFFF
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "약관동의(전체)",
                        fontSize = 14.sp,
                        color = ColorUtils.gray_222222,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .padding(start = 10.5.dp)
                            .weight(1f)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorUtils.gray_E1E1E1)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    CheckPolicy(text = "이용약관 동의", check1)
                    ContentPolicy(content = "(Not provider)")

                    CheckPolicy(text = "개인정보 이용 수집 방침", check2)
                    ContentPolicy(content = "(Not provider)")

                    CheckPolicy(text = "위치정보 제공 동의", check3)
                    ContentPolicy(content = "(Not provider)")

                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    val tick1 = remember { mutableStateOf(false) }
                    val tick2 = remember { mutableStateOf(false) }
                    TickCustom("이벤트 및 혜택 알림 수신 동의(선택)", tick1)
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    TickCustom("예약 및 서비스 알림 수신 동의(선택)", tick2)
                    Spacer(modifier = Modifier.padding(bottom = 40.dp))
                }
            }
            Button(
                onClick = { Log.e("PHUC", "click dc") },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (checkedAll.value) ColorUtils.blue_2177E4 else ColorUtils.gray_E1E1E1
                ),
                enabled = checkedAll.value,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "가입하기",
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 18.sp,
                )
            }
        }
    }

    @Composable
    fun CheckPolicy(text: String, checked: MutableState<Boolean>) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 17.dp)
        ) {
//            val checked = remember { mutableStateOf(false) }
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
        }
    }

    @Composable
    fun ContentPolicy(content: String) {
        Column(
            Modifier
                .padding(top = 8.dp)
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

    @Composable
    fun TickCustom(text: String, ticked: MutableState<Boolean>) {
//        remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.noRippleClickable {
                ticked.value = !ticked.value
            }) {
            Image(
                painter = painterResource(R.drawable.ic_tick),
                contentDescription = "",
                colorFilter = if (ticked.value) ColorFilter.tint(ColorUtils.gray_222222)
                else ColorFilter.tint(ColorUtils.gray_E1E1E1),
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Text(
                text,
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }
}