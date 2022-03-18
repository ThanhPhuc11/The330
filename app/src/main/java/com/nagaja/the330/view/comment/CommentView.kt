package com.nagaja.the330.view.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.model.CommentModel
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.text14_222
import com.nagaja.the330.view.text14_62


@Composable
fun CommentComponent() {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
    val viewModel = ViewModelProvider(
        viewModelStoreOwner,
        ViewModelFactory(
            RetrofitBuilder.getInstance(context)
                ?.create(ApiService::class.java)!!
        )
    )[CommentVM::class.java]

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                }
                Lifecycle.Event.ON_STOP -> {

                }
                else -> {}
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun CommentList(
    count: Int? = 0,
    listComment: SnapshotStateList<CommentModel>,
    lazyListState: LazyListState,
    userId: Int,
    onEdit: (Int) -> Unit
) {
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

        LazyColumn(
            state = lazyListState,
//        verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.heightIn(0.dp, 500.dp)
        ) {
            itemsIndexed(listComment) { index, obj ->
                if (obj.writer?.id != userId) {
                    ItemComment(obj)
                } else {
                    ItemCommentOwner(obj) {
                        onEdit.invoke(obj.id!!)
                    }
                }
                Divider(color = ColorUtils.gray_E1E1E1)
            }
        }
    }
}

@Composable
fun CommentInput(onPost: (String) -> Unit) {
    val textStateId: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue(""))
    }
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
            BasicTextField(
                value = textStateId.value,
                onValueChange = {
                    if (it.text.length <= 200) textStateId.value = it
                },
                Modifier
                    .weight(1f),
                singleLine = true,
                textStyle = TextStyle(
                    color = ColorUtils.black_000000
                ),
                decorationBox = { innerTextField ->
                    Row {
                        if (textStateId.value.text.isEmpty()) {
                            Text(
                                "댓글을 입력해 보세요.",
                                color = ColorUtils.gray_BEBEBE,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    innerTextField()
                }
            )

            Image(
                painter = painterResource(R.drawable.ic_post_comment),
                contentDescription = null,
                modifier = Modifier.noRippleClickable {
                    onPost.invoke(textStateId.value.text)
                    textStateId.value = TextFieldValue("")
                }
            )
        }
    }
}

@Composable
private fun ItemComment(obj: CommentModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(ColorUtils.white_FFFFFF)
            .padding(16.dp)
    ) {
        Text("${obj.writer?.name}", style = text14_222)
        Text(
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_16,
                AppDateUtils.FORMAT_15,
                obj.createdOn ?: ""
            ), color = ColorUtils.gray_9F9F9F, fontSize = 12.sp
        )
        Text("${obj.body}", style = text14_62, modifier = Modifier.padding(top = 5.dp))
    }
}

@Composable
private fun ItemCommentOwner(obj: CommentModel, onEdit: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(ColorUtils.blue_2177E4_opacity_5)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("${obj.writer?.name}", style = text14_222)
            Spacer(Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.ic_more), contentDescription = null,
                modifier = Modifier.noRippleClickable {
                    onEdit.invoke()
                })
        }
        Text(
            AppDateUtils.changeDateFormat(
                AppDateUtils.FORMAT_16,
                AppDateUtils.FORMAT_15,
                obj.createdOn ?: ""
            ), color = ColorUtils.gray_9F9F9F, fontSize = 12.sp
        )
        Text("${obj.body}", style = text14_62, modifier = Modifier.padding(top = 5.dp))
    }
}