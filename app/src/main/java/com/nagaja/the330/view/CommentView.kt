package com.nagaja.the330.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.R
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.utils.ColorUtils

@Preview
@Composable
fun CommentList(count: Int? = 0) {
    Column(Modifier.fillMaxWidth()) {
        Divider(
            color = ColorUtils.gray_F5F5F5,
            modifier = Modifier.height(8.dp)
        )
        Text(
            "댓글 ${count ?: 0}",
            style = text14_62,
            modifier = Modifier.padding(16.dp)
        )

        val listComment = remember {
            mutableListOf<KeyValueModel>().apply {
                add(KeyValueModel("0", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("0", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
                add(KeyValueModel("1", ""))
            }
        }
        LazyColumn(
            state = rememberLazyListState(),
//        verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.heightIn(0.dp, 500.dp)
        ) {
            itemsIndexed(listComment) { index, obj ->
                if (obj.id == "1") {
                    ItemComment()
                } else {
                    ItemCommentOwner()
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }
}

@Preview
@Composable
fun CommentInput() {
    Box(Modifier.padding(vertical = 20.dp, horizontal = 16.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(44.dp)
                .border(
                    width = 1.dp,
                    color = ColorUtils.gray_E1E1E1,
                    shape = RoundedCornerShape(99.dp)
                )
                .background(
                    ColorUtils.gray_F5F5F5,
                    shape = RoundedCornerShape(99.dp)
                )
                .padding(2.dp)
                .padding(start = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "댓글을 입력해 보세요.",
                color = ColorUtils.gray_BEBEBE,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Image(
                painter = painterResource(R.drawable.ic_post_comment),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun ItemComment() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(ColorUtils.white_FFFFFF)
            .padding(16.dp)
    ) {
        Text("mintkim", style = text14_222)
        Text("2021. 10. 18", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp)
        Text("우와 넘 좋은 소식이네요.", style = text14_62, modifier = Modifier.padding(top = 5.dp))
    }
}

@Preview
@Composable
private fun ItemCommentOwner() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(ColorUtils.blue_2177E4_opacity_5)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("mintkim", style = text14_222)
            Spacer(Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.ic_more), contentDescription = null,
                modifier = Modifier.noRippleClickable {

                })
        }
        Text("2021. 10. 18", color = ColorUtils.gray_9F9F9F, fontSize = 12.sp)
        Text("우와 넘 좋은 소식이네요.", style = text14_62, modifier = Modifier.padding(top = 5.dp))
    }
}