package com.nagaja.the330.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.utils.ColorUtils

@Preview(showBackground = true)
@Composable
fun Preview() {
    Column(Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {}
        .background(ColorUtils.white_FFFFFF)) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Text(
            "로그인",
            color = ColorUtils.gray_222222,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = screenHeight / 10)
        )
        Text(
            "아이디",
            color = ColorUtils.gray_222222,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 44.dp),
            fontWeight = FontWeight.SemiBold
        )
        val textStateId = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = textStateId.value,
            onValueChange = { if (it.text.length <= 20) textStateId.value = it },
            placeholder = {
                Text(
                    text = "아이디를 입력해 주세요.",
                    fontSize = 14.sp,
                    color = ColorUtils.gray_BEBEBE
                )
            },
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .background(ColorUtils.white_FFFFFF)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ColorUtils.white_FFFFFF,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )

        Text(
            "비밀번호",
            color = ColorUtils.gray_222222,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp),
            fontWeight = FontWeight.SemiBold
        )
        val textStatePass = remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = textStatePass.value,
            onValueChange = { if (it.text.length <= 15) textStatePass.value = it },
            placeholder = {
                Text(
                    text = "비밀번호를 입력해주세요.",
                    fontSize = 14.sp,
                    color = ColorUtils.gray_BEBEBE
                )
            },
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .background(ColorUtils.white_FFFFFF)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(4.dp)
                ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ColorUtils.white_FFFFFF,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .height(48.dp)
                .background(ColorUtils.blue_2177E4)
        ) {
            Text(
                "로그인",
                color = ColorUtils.white_FFFFFF,
                fontSize = 18.sp
            )
        }
        Row(Modifier.padding(top = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("아이디 찾기", style = text1, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(1.dp)
                    .background(ColorUtils.gray_E1E1E1)
            )
            Text("비밀번호 찾기", style = text1, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(1.dp)
                    .background(ColorUtils.gray_E1E1E1)
            )
            Text("회원가입", style = text1, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.weight(1f))
        LoginSocial()
    }
}

val text1 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.gray_626262,
    fontSize = 14.sp,
    textAlign = TextAlign.Center
)

@Preview(showBackground = true)
@Composable
fun LoginSocial() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            "SNS 계정으로 회원가입/로그인",
            fontSize = 14.sp,
            color = ColorUtils.gray_222222
        )
        Row(Modifier.padding(top = 20.dp)) {
            IconLogin(ColorUtils.yellow_FFCD00, R.drawable.ic_kakao)
            IconLogin(ColorUtils.green_1EC800, R.drawable.ic_naver)
            Image(
                painter = painterResource(R.drawable.ic_google_login),
                contentDescription = "",
                Modifier
                    .padding(8.dp)
                    .size(48.dp)
            )
            IconLogin(ColorUtils.blue_3B5998, R.drawable.ic_facebook)
        }
        Text(
            "비회원으로 이용하기",
            color = ColorUtils.gray_626262,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 32.dp, bottom = 37.dp)
        )
    }
}

@Composable
fun IconLogin(color: Color, drawable: Int) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .size(48.dp)
            .background(color = color),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(drawable), contentDescription = "")
    }
}