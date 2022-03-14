package com.nagaja.the330.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.utils.ColorUtils

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val notoSansKrRegular12 = TextStyle(
    color = ColorUtils.gray_9F9F9F,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    fontFamily = FontFamily(Font(R.font.notosans_kr_regular))
)

val textRegular12 = TextStyle(
    color = ColorUtils.gray_9F9F9F,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
)

val notoSansKrBold16 = TextStyle(
    color = ColorUtils.gray_222222,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily(Font(R.font.notosans_kr_bold))
)

val textBold16 = TextStyle(
    color = ColorUtils.gray_222222,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
)

val textBold18 = TextStyle(
    color = ColorUtils.gray_222222,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
)