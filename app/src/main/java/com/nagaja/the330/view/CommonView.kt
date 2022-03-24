package com.nagaja.the330.view

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils

@Composable
fun LayoutTheme330(modifier: Modifier = Modifier, layout: @Composable ColumnScope.() -> Unit) {
//    val focusManager = LocalFocusManager.current
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = ColorUtils.white_FFFFFF
    )
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

@Preview(showBackground = true)
@Composable
fun HeaderSearch(
    clickBack: (() -> Unit)? = null,
    clickSearch: ((String) -> Unit)? = null,
    textOption: String? = null,
    clickOption: (() -> Unit)? = null
) {
    val stateEdtInput = remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .background(ColorUtils.white_FFFFFF)
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "",
            Modifier
                .padding(horizontal = 19.dp)
                .noRippleClickable { clickBack?.invoke() }
        )
        Row(
            Modifier
                .padding(end = 16.dp)
                .weight(1f)
                .height(36.dp)
                .background(ColorUtils.blue_2177E4_opacity_5)
                .border(
                    width = 1.dp,
                    color = ColorUtils.blue_2177E4_opacity_10,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 9.dp)
                    .noRippleClickable {
                        clickSearch?.invoke(stateEdtInput.value.text)
                    }
            )
            BasicTextField(
                value = stateEdtInput.value,
                onValueChange = {
                    if (it.text.length <= 100) stateEdtInput.value = it
                },
                Modifier
                    .fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = {
                    clickSearch?.invoke(stateEdtInput.value.text)
                }),
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (stateEdtInput.value.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.please_enter_search_term),
                                color = ColorUtils.gray_565656,
                                fontSize = 14.sp
                            )
                        }
                    }
                    innerTextField()
                }
            )
        }
        AnimatedVisibility(visible = textOption != null) {
            Text(
                textOption ?: "",
                color = ColorUtils.blue_2177E4,
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .noRippleClickable {
                        clickOption?.invoke()
                    }
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

@Composable
fun DialogReport(
    state: MutableState<Boolean>,
    onClick: ((Boolean, String) -> Unit)?
) {
    val textStateReason = remember { mutableStateOf(TextFieldValue("")) }
    Dialog(
        onDismissRequest = { state.value = false },
        content = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.report_it),
                        color = ColorUtils.black_000000,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(top = 28.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    TextField(
                        value = textStateReason.value,
                        onValueChange = { if (it.text.length <= 1000) textStateReason.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(
                                width = 1.dp,
                                color = ColorUtils.gray_E1E1E1,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = ColorUtils.white_FFFFFF,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.direct_input_reason_for_reporting),
                                fontSize = 14.sp,
                                color = ColorUtils.gray_BEBEBE
                            )
                        }
                    )

                    Text(
                        stringResource(R.string.display_notice_report),
                        color = ColorUtils.gray_626262,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
                    )
                }
                //TODO: Button
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(ColorUtils.gray_222222)
                            .noRippleClickable {
                                state.value = false
                                onClick?.invoke(false, "")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(ColorUtils.blue_2177E4)
                            .noRippleClickable {
                                state.value = false
                                onClick?.invoke(true, textStateReason.value.text)
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.confirm),
                            color = ColorUtils.white_FFFFFF,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    )
}

@Composable
fun DialogCancelComment(
    state: MutableState<Boolean>,
    onClick: ((Boolean) -> Unit)?
) {
    Dialog(
        onDismissRequest = { state.value = false },
        content = {
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    Modifier
                        .padding(bottom = 7.dp)
                        .fillMaxWidth()
                        .background(
                            color = ColorUtils.white_FFFFFF,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "댓글 더보기",
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Divider(color = ColorUtils.gray_E1E1E1)
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                state.value = false
                                onClick?.invoke(true)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "삭제",
                            color = ColorUtils.blue_2177E4,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Box(
                    Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .height(57.dp)
                        .background(
                            color = ColorUtils.white_FFFFFF,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .noRippleClickable {
                            state.value = false
                            onClick?.invoke(false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "취소",
                        color = ColorUtils.blue_2177E4,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    )
}

@Preview
@Composable
fun view1() {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "신고하기",
                color = ColorUtils.black_000000,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            val textStateId = remember { mutableStateOf(TextFieldValue("")) }
            TextField(
                value = textStateId.value,
                onValueChange = { if (it.text.length <= 200) textStateId.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(
                        width = 1.dp,
                        color = ColorUtils.gray_E1E1E1,
                        shape = RoundedCornerShape(4.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ColorUtils.white_FFFFFF,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "신고사유 직접 입력",
                        fontSize = 14.sp,
                        color = ColorUtils.gray_BEBEBE
                    )
                }
            )

            Text(
                "※ 관리자에게 신고사유가 전달되며 관리자 확인 후 처리됩니다.",
                color = ColorUtils.gray_626262,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )
        }
        //TODO: Button
        Row(
            Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(ColorUtils.gray_222222),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "취소",
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {

                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    "확인",
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
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

@Composable
fun TabSelected(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    if (isSelected)
        Box(
            modifier
                .background(ColorUtils.white_FFFFFF)
                .fillMaxHeight()
                .noRippleClickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = ColorUtils.gray_222222, fontSize = 16.sp)
        }
    else
        Box(
            modifier
                .background(ColorUtils.gray_222222)
                .fillMaxHeight()
                .noRippleClickable {
                    onClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = ColorUtils.white_FFFFFF,
                fontSize = 16.sp,
                modifier = Modifier.alpha(0.4f)
            )
        }
}

@Composable
fun HandleSortUI(context: Context, ft: MutableList<KeyValueModel>, onClick: (KeyValueModel) -> Unit) {
    val filters = remember { ft }
    var expanded by remember { mutableStateOf(false) }
    val itemSelected = remember { mutableStateOf(filters[0]) }
    Row(
        Modifier
            .size(100.dp, 36.dp)
            .border(width = 1.dp, color = ColorUtils.gray_E1E1E1)
            .padding(9.dp)
            .noRippleClickable {
                expanded = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            itemSelected.value.name ?: "",
            modifier = Modifier.weight(1f),
            style = text14_222
        )
        Image(
            painterResource(R.drawable.ic_arrow_down), null,
            Modifier
                .rotate(if (expanded) 180f else 0f)
                .width(10.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            filters.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        itemSelected.value = option
                        expanded = false
                        onClick.invoke(option)
                        if (BuildConfig.DEBUG)
                            Toast.makeText(context, "${itemSelected.value.id}", Toast.LENGTH_LONG)
                                .show()
                    }
                ) {
                    option.name?.let { Text(text = it) }
                }

            }
        }
    }
}

fun showMessDebug(mess: String?, context: Context) {
    if (BuildConfig.DEBUG) Toast.makeText(context, mess ?: "", Toast.LENGTH_SHORT).show()
}