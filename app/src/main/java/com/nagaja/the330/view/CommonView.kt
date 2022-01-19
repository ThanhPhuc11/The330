package com.nagaja.the330.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.utils.ColorUtils

@Preview(showBackground = true)
@Composable
fun Header(title: String = "title", clickBack: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .height(44.dp),
    ) {
        Text(
            title,
            fontSize = 17.sp,
            color = ColorUtils.black_000000,
            modifier = Modifier.align(Alignment.Center)
        )
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "",
            Modifier
                .padding(horizontal = 19.dp)
                .align(Alignment.CenterStart)
                .noRippleClickable { clickBack?.invoke() }
        )
    }
}

@Preview
@Composable
fun Test() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        TextFieldCustom()
    }
}

@Composable
fun TextFieldCustom(modifier: Modifier = Modifier, hint: String = "") {
    val textStateId = remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .height(44.dp)
            .border(
                width = 1.dp,
                color = ColorUtils.gray_E1E1E1,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textStateId.value,
            onValueChange = { if (it.text.length <= 20) textStateId.value = it },
            Modifier
                .fillMaxWidth(),
            singleLine = true,
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

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

val text14_62 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.gray_626262,
    fontSize = 14.sp,
    textAlign = TextAlign.Center
)

val text14_222 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.gray_222222,
    fontSize = 14.sp,
    textAlign = TextAlign.Center
)