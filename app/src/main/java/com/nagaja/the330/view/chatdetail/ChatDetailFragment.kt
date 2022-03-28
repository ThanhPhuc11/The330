package com.nagaja.the330.view.chatdetail

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.ItemMessageModel
import com.nagaja.the330.model.RoomDetailModel
import com.nagaja.the330.model.StartChatRequest
import com.nagaja.the330.utils.*
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable
import com.nagaja.the330.view.reservationregis.ReservationRegisFragment
import com.skydoves.landscapist.glide.GlideImage

class ChatDetailFragment : BaseFragment() {
    private lateinit var viewModel: ChatDetailVM
    private var partnerId: Int? = null
    private var roomId: Int? = null
    private var type: String? = null
    private var postId: String? = null
    private var isFirst = true
    private var onAddToBottom: (() -> Unit)? = null
    private lateinit var database: DatabaseReference

    companion object {
//        fun newInstance(partnerId: Int? = null, roomId: Int? = null) = ChatDetailFragment().apply {
//            arguments = Bundle().apply {
//                partnerId?.let { putInt(AppConstants.EXTRA_KEY1, it) }
//                roomId?.let { putInt(AppConstants.EXTRA_KEY2, it) }
//            }
//        }

        fun newInstance(
            type: String? = null,
            postId: String? = null,
            partnerId: Int? = null,
            roomId: Int? = null
        ) =
            ChatDetailFragment().apply {
                arguments = Bundle().apply {
                    partnerId?.let { putInt(AppConstants.EXTRA_KEY1, it) }
                    roomId?.let { putInt(AppConstants.EXTRA_KEY2, it) }
                    type?.let { putString(AppConstants.EXTRA_KEY3, it) }
                    postId?.let { putString(AppConstants.EXTRA_KEY4, it) }
                }
            }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[ChatDetailVM::class.java]
        viewController = (activity as MainActivity).viewController

        database = Firebase.database.reference
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        partnerId = requireArguments().getInt(AppConstants.EXTRA_KEY1, -1)
                        roomId = requireArguments().getInt(AppConstants.EXTRA_KEY2, -1)
                        type = requireArguments().getString(AppConstants.EXTRA_KEY3)
                        postId = requireArguments().getString(AppConstants.EXTRA_KEY4)
                        if (roomId != null && roomId!! > 0) {
                            viewModel.startChatByRoomId(
                                accessToken!!,
                                roomId!!
                            )
                        } else {
                            when (type) {
                                AppConstants.RECRUITMENT -> {
                                    viewModel.startChat(
                                        accessToken!!,
                                        StartChatRequest().apply {
                                            userId = partnerId
                                            recruitmentId = postId
                                        }
                                    )
                                }

                                AppConstants.SECONDHAND -> {
                                    viewModel.startChat(
                                        accessToken!!,
                                        StartChatRequest().apply {
                                            userId = partnerId
                                            secondHandPostId = postId
                                        }
                                    )
                                }

                                else -> {
//                                    if (partnerId != null && partnerId != -1) {
                                    viewModel.startChat(
                                        accessToken!!,
                                        StartChatRequest().apply { userId = partnerId })
//                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        LaunchedEffect(viewModel.stateRoomInfo.value) {
            viewModel.stateIsSeller.value =
                userDetailBase?.id == viewModel.stateRoomInfo.value.target?.id
            viewModel.stateRoomInfo.value.id?.let {
                listenRealtime()
                viewModel.getChatDetail(accessToken!!, it)
            }
        }
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            //TODO: Info
            if (type.isNullOrEmpty()) {
                InfoFriend(viewModel.stateRoomInfo.value)
                Divider(
                    color = ColorUtils.gray_E1E1E1,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                )
            }

            //TODO: Content Mess
            val listMess = viewModel.stateListMess
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (listMess.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(R.drawable.ic_empty_mess_detail), "")
                        Text(
                            "대화를 시작해보세요.",
                            color = ColorUtils.gray_9B9A99,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState,
                    reverseLayout = false,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    itemsIndexed(listMess) { index, obj ->
                        if (obj.userId?.toInt() == userDetailBase?.id || obj.user?.id == userDetailBase?.id) {
                            ItemMeChat(obj)
                        } else {
                            ItemYouChat(obj)
                        }
//                        ItemCapture()
                    }
                }
                LaunchedEffect(viewModel.stateBottomItem.value) {
                    lazyListState.animateScrollToItem(viewModel.stateListMess.size)
                }

                LoadmoreMessHandler(lazyListState) { page ->
                    viewModel.stateRoomInfo.value.id?.let {
                        viewModel.getChatDetail(accessToken!!, it, viewModel.stateListMess[0].id)
                    }
                }
            }

            //TODO: Footer
            InputChat()
            Row(Modifier.fillMaxWidth()) {
                //TODO: make reservation
                if (!viewModel.stateIsSeller.value) {
                    ButtonFooter(
                        modifier = Modifier.weight(1f),
                        text = "예약하기",
                        ColorUtils.blue_2177E4
                    ) {
                        showMessDEBUG("make reservation")
                        viewModel.stateRoomInfo.value.target?.id?.let {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_RESERVATION_REGIS,
                                ReservationRegisFragment.newInstance(it)
                            )
                        }
                    }
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

    @Composable
    private fun InfoFriend(roomInfo: RoomDetailModel) {
        if (viewModel.stateIsSeller.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(Modifier.padding(start = 13.dp)) {
                    Text(
                        "${roomInfo.actor?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp
                    )
                    Text(
                        "${roomInfo.createdOn}",
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                GlideImage(
                    imageModel = "${BuildConfig.BASE_S3}${roomInfo.companyOwner}",
                    Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    placeHolder = painterResource(R.drawable.ic_default_nagaja),
                    error = painterResource(R.drawable.ic_default_nagaja)
                )
                Column(Modifier.padding(start = 13.dp)) {
                    Text(
                        "${roomInfo.companyOwner?.name?.getOrNull(0)?.name}",
                        color = ColorUtils.gray_222222,
                        fontSize = 16.sp
                    )
                    Text(
                        "${roomInfo.createdOn}",
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    private fun InputChat() {
        val stateInputChat = remember { mutableStateOf(TextFieldValue("")) }
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
                BasicTextField(
                    value = stateInputChat.value,
                    onValueChange = {
                        if (it.text.length <= 50) stateInputChat.value = it
                    },
                    Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = ColorUtils.black_000000
                    ),
                    decorationBox = { innerTextField ->
                        Row {
                            if (stateInputChat.value.text.isEmpty()) {
                                Text(
                                    "메세지를 입력해 주세요.",
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
            }

            Image(
                painterResource(R.drawable.ic_sent_chat),
                null,
                modifier = Modifier
                    .padding(start = 6.dp, end = 2.dp)
                    .noRippleClickable {
                        viewModel.sendMess(accessToken!!, ItemMessageModel().apply {
                            chatRoomId = viewModel.stateRoomInfo.value.id
                            message = stateInputChat.value.text
                            type = "INIT"
                        })
                        stateInputChat.value = TextFieldValue("")
                    }
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

    @Composable
    private fun ItemMeChat(obj: ItemMessageModel) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .noRippleClickable {
                    showMessDEBUG(obj.id.toString())
                },
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
                    Text("${obj.message}", color = ColorUtils.white_FFFFFF, fontSize = 14.sp)
                }
                Row(Modifier.width(200.dp)) {
                    Text(
                        "전송됨",
                        color = ColorUtils.gray_9E9E9E,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        obj.createdOn ?: "", color = ColorUtils.gray_9E9E9E, fontSize = 12.sp
                    )
                }
            }
        }
    }

    @Composable
    private fun ItemYouChat(obj: ItemMessageModel) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .noRippleClickable {
                    showMessDEBUG(obj.id.toString())
                },
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
                    Text("${obj.message}", color = ColorUtils.black_000000, fontSize = 14.sp)
                }
                Row(Modifier.width(200.dp)) {
                    Text(
                        "${obj.createdOn}",
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

    private fun listenRealtime() {
        database.child("chat_rooms").child(viewModel.stateRoomInfo.value.id.toString())
            .child("messages").limitToLast(1).addChildEventListener(eventListenerRealtime)
    }

    private val eventListenerRealtime = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val newMess = ItemMessageModel().apply {
                messageId = snapshot.child("messageId").value as Long?
                userId = (snapshot.child("userId").value as String?)
                name = (snapshot.child("name").value as String?)
                message = (snapshot.child("message").value as String?)
                createdOn = try {
                    AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_7,
                        AppDateUtils.FORMAT_5,
                        AppDateUtils.convertTime(snapshot.child("createdOn").value as Long)
                    )
                } catch (e: Exception) {
                    ""
                }
            }
            if (isFirst) {
                isFirst = false
            } else {
                viewModel.stateListMess.add(viewModel.stateListMess.size, newMess)
                viewModel.stateBottomItem.value = newMess
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
}