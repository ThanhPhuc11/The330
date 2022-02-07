package com.nagaja.the330.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Preview
@Composable
fun HomeScreen() {
    LayoutTheme330 {
        LogoAndSearch()
        SearchFilter()
        IconCategory()
    }
}

@Composable
private fun LogoAndSearch() {
    Row(
        Modifier
            .padding(vertical = 7.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_png),
            contentDescription = "",
            colorFilter = ColorFilter.tint(ColorUtils.blue_2177E4),
            modifier = Modifier.height(16.dp)
        )
        Row(
            Modifier
                .padding(start = 18.dp)
                .height(32.dp)
                .weight(1f)
//                .clip(RoundedCornerShape(4.dp))
                .background(ColorUtils.blue_2177E4_opacity_5, RoundedCornerShape(4.dp))
                .border(1.dp, ColorUtils.blue_2177E4_opacity_10, RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "",
                Modifier.padding(horizontal = 10.dp)
            )
            Text(
                "검색어를 입력해 보세요.",
                color = ColorUtils.gray_626262,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SearchFilter() {
    Row(
        Modifier.padding(horizontal = 16.dp)
    ) {
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(
            modifier = Modifier
                .weight(1f)
                .padding(end = 7.dp),
            options = GetDummyData.getCoutryAdrressSignup()
        )
        BoxSearch(modifier = Modifier.weight(1f), options = GetDummyData.getCoutryAdrressSignup())
    }
}

@Composable
private fun BoxSearch(modifier: Modifier = Modifier, options: MutableList<KeyValueModel>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    Row(
        modifier = modifier
            .noRippleClickable {
                expanded = !expanded
            }
            .border(
                width = 1.dp,
                color = ColorUtils.blue_2177E4_opacity_10,
                shape = RoundedCornerShape(4.dp)
            )
            .height(44.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            selectedOptionText.name!!,
            style = text14_222,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow_filter),
            contentDescription = "",
            Modifier.rotate(if (expanded) 180f else 0f)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.name!!)
                }
            }
        }
    }
}

@Preview
@Composable
fun IconCategory() {
    Column() {
        GlideImage(
            imageModel = "https://bomcook.s3.ap-northeast-2.amazonaws.com/banner/th1.jpg",
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.None,
//            circularReveal = CircularReveal(duration = 250),
            placeHolder = ImageBitmap.imageResource(R.drawable.ic_google),
            error = ImageBitmap.imageResource(R.drawable.ic_google),
            modifier = Modifier.height(100.dp).background(ColorUtils.pink_FF1E54)
        )
        Text(
            "HAHA",
            fontSize = 12.sp,
            color = ColorUtils.gray_222222
        )
    }
}