package com.nagaja.the330.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.painterResource
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

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}