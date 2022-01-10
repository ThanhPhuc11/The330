package com.nagaja.the330.view.language

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment

class LangFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = LangFragment()
    }

    @Composable
    override fun UIData() {
        Column {
            Text(
                text = "NAGAJA",
                color = colorResource(R.color.blue_2177E4)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUI() {
    Column(
        Modifier
            .padding(48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NAGAJA",
            color = colorResource(R.color.blue_2177E4)
        )
        selectLang("한국어")
        selectLang("한국어")
        selectLang("한국어")
        selectLang("한국어")
    }
}

@Composable
fun selectLang(str: String) {
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .padding(bottom = 7.dp)
            .fillMaxWidth()
            .height(52.dp)
            .border(1.dp, colorResource(R.color.gray_E1E1E1)),
    ) {
        Text(
            text = str,
            textAlign = TextAlign.Center,
        )
    }
}