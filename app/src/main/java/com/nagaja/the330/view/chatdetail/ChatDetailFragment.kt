package com.nagaja.the330.view.chatdetail

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.KeyValueModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatDetailFragment : BaseFragment() {
    private lateinit var viewModel: ChatDetailVM

    companion object {
        fun newInstance() = ChatDetailFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ChatDetailVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            //TODO: Info
            InfoFriend()
            Divider(
                color = ColorUtils.gray_E1E1E1,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )

            //TODO: Content Mess
            val listMess = remember {
                mutableStateListOf<KeyValueModel>().apply {
                    add(KeyValueModel())
                    add(KeyValueModel())
                    add(KeyValueModel())
                }
            }
            LazyColumn(
                state = rememberLazyListState(),
                reverseLayout = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(listMess) { index, obj ->
                    ItemCapture()
                }
            }

            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(5000)
                    listMess.add(KeyValueModel())
                }
            }

            //TODO: Footer
            InputChat()
            Row(Modifier.fillMaxWidth()) {
                //TODO: make reservation
                ButtonFooter(
                    modifier = Modifier.weight(1f),
                    text = "예약하기",
                    ColorUtils.blue_2177E4
                ) {
                    showMessDEBUG("make reservation")
                }

                //TODO: chatting end
                ButtonFooter(
                    modifier = Modifier.weight(1f),
                    text = "채팅종료",
                    ColorUtils.gray_222222
                ) {
                    showMessDEBUG("end chat")
                }
            }
        }
    }

    @Preview
    @Composable
    private fun InfoFriend(userDetail: UserDetail = UserDetail()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GlideImage(
                imageModel = "${BuildConfig.BASE_S3}${""}",
                Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(4.dp)),
                placeHolder = painterResource(R.drawable.ic_default_nagaja),
                error = painterResource(R.drawable.ic_default_nagaja)
            )
            Column(Modifier.padding(start = 13.dp)) {
                Text("Name", color = ColorUtils.gray_222222, fontSize = 16.sp)
                Text(
                    "2021. 10. 20, 14:22",
                    color = ColorUtils.gray_9F9F9F,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }

    @Preview
    @Composable
    private fun InputChat() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .weight(1f)
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
                    "메세지를 입력해 주세요.",
                    color = ColorUtils.gray_BEBEBE,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }

            Image(
                painterResource(R.drawable.ic_sent_chat),
                null,
                modifier = Modifier.padding(start = 6.dp, end = 2.dp)
            )
        }
    }

    @Composable
    private fun ButtonFooter(
        modifier: Modifier = Modifier,
        text: String,
        color: Color,
        onClick: () -> Unit
    ) {
        Box(
            modifier
                .height(52.dp)
                .background(color)
                .noRippleClickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = ColorUtils.white_FFFFFF,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }


    @Preview
    @Composable
    private fun ItemMeChat() {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column {
                Box(
                    Modifier
                        .width(200.dp)
                        .background(
                            color = ColorUtils.gray_212121,
                            shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
                        )
                        .padding(12.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Text("hahaha", color = ColorUtils.white_FFFFFF, fontSize = 14.sp)
                }
                Row(Modifier.width(200.dp)) {
                    Text(
                        "읽음",
                        color = ColorUtils.gray_9E9E9E,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text("2021. 10. 22, 16:08 PM", color = ColorUtils.gray_9E9E9E, fontSize = 12.sp)
                }
            }
        }
    }

    @Preview
    @Composable
    private fun ItemYouChat() {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Box(
                    Modifier
                        .width(200.dp)
                        .background(
                            color = ColorUtils.gray_EEEEEE,
                            shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
                        )
                        .padding(12.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Text("hahaha", color = ColorUtils.white_FFFFFF, fontSize = 14.sp)
                }
                Row(Modifier.width(200.dp)) {
                    Text(
                        "2021. 10. 22, 16:08 PM",
                        color = ColorUtils.gray_9E9E9E,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text("읽음", color = ColorUtils.gray_9E9E9E, fontSize = 12.sp)
                }
            }
        }
    }

    @Preview
    @Composable
    private fun ItemCapture() {
        Box(
            Modifier
                .padding(bottom = 40.dp)
                .fillMaxWidth()
                .background(ColorUtils.blue_E9F1FC)
                .padding(vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "YYYY-MM-DD HH:MM\n" +
                        "{user_id}님이 대화 내용을 캡쳐했습니다.",
                color = ColorUtils.black_000000,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}