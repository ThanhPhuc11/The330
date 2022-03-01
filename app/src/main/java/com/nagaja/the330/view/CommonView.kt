package com.nagaja.the330.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nagaja.the330.R
import com.nagaja.the330.utils.ColorUtils

@Composable
fun LayoutTheme330(modifier: Modifier = Modifier, layout: @Composable ColumnScope.() -> Unit) {
//    val focusManager = LocalFocusManager.current
//    val systemUiController = rememberSystemUiController()
//    systemUiController.setSystemBarsColor(
//        color = ColorUtils.white_FFFFFF
//    )
    Column(
        modifier = modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxSize()
            .noRippleClickable { }
    ) {
        layout(this)
    }
}

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

@Preview(showBackground = true)
@Composable
fun HeaderOption(
    title: String = "title",
    clickBack: (() -> Unit)? = null,
    optionText: String = "right",
    clickOption: (() -> Unit)? = null
) {
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
        Text(
            optionText,
            fontSize = 14.sp,
            color = ColorUtils.blue_2177E4,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 16.dp)
                .noRippleClickable { clickOption?.invoke() }
        )
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
    focusable: Boolean = true
) {
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

@Composable
fun Dialog2Button(
    state: MutableState<Boolean>,
    title: String?,
    content: String?,
    leftText: String?,
    rightText: String?,
    onClick: ((Boolean) -> Unit)?
) {
    Dialog(
        onDismissRequest = { state.value = false },
        content = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = ColorUtils.gray_F2F2F2, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 19.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        title ?: "",
                        color = ColorUtils.black_000000,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        content ?: "",
                        color = ColorUtils.black_000000,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(ColorUtils.gray_3C3C43)
                )
                Row {
                    Box(
                        modifier = Modifier
                            .height(43.dp)
                            .weight(1f)
                            .noRippleClickable {
                                state.value = false
                                onClick?.invoke(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(leftText ?: "", color = ColorUtils.blue_007AFF, fontSize = 17.sp)
                    }
                    Box(
                        Modifier
                            .height(43.dp)
                            .width(1.dp)
                            .background(ColorUtils.gray_626262)
                    )
                    Box(
                        modifier = Modifier
                            .height(43.dp)
                            .weight(1f)
                            .noRippleClickable {
                                state.value = false
                                onClick?.invoke(true)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            rightText ?: "",
                            color = ColorUtils.blue_007AFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,

            )
    )
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    val focusManager = LocalFocusManager.current
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
        focusManager.clearFocus()
    }
}

val text14_62 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.gray_626262,
    fontSize = 14.sp,
    textAlign = TextAlign.Center
)

val text16_black = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.black_000000,
    fontSize = 16.sp
)

val text14_222 = TextStyle(
//    fontFamily = FontFamily.Default,
//    fontWeight = FontWeight.Light,
    color = ColorUtils.gray_222222,
    fontSize = 14.sp,
    textAlign = TextAlign.Center
)