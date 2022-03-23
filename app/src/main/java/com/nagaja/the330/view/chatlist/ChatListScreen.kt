package com.nagaja.the330.view.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.ViewController
import com.nagaja.the330.base.ViewModelFactory
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.RoomDetailModel
import com.nagaja.the330.model.UserDetail
import com.nagaja.the330.network.ApiService
import com.nagaja.the330.network.RetrofitBuilder
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.chatdetail.ChatDetailFragment
import com.nagaja.the330.view.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ChatListScreen(accessToken: String, viewController: ViewController?, user: UserDetail) {
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
    )[ChatListVM::class.java]
    val options =
        if (user.userType == AppConstants.GENERAL) GetDummyData.getSortReservation(context)
        else GetDummyData.getSortReservationRoleCompany(context)

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.sort = options[0].id!!
                    viewModel.getChatList(
                        accessToken,
                        0
                    )
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

    LayoutTheme330 {
        Header("상담현황")
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "총 40건",
                color = ColorUtils.black_000000,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )


            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }
            Row(
                Modifier
                    .size(100.dp, 36.dp)
                    .border(width = 1.dp, color = ColorUtils.gray_E1E1E1)
                    .padding(horizontal = 9.dp)
                    .noRippleClickable {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedOptionText.name ?: "",
                    color = ColorUtils.black_000000,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    Modifier
                        .rotate(if (expanded) 180f else 0f)
                        .width(10.dp),
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
                                viewModel.sort = selectionOption.id!!
                                viewModel.getChatList(accessToken, 0)
                            }
                        ) {
                            Text(text = selectionOption.name!!)
                        }
                    }
                }
            }
        }

        Divider(color = ColorUtils.gray_E1E1E1)
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val listChat = viewModel.stateListRoom
            val lazyListState = rememberLazyListState()
            if (listChat.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(R.drawable.ic_empty_mess_detail), "")
                    Text(
                        "채팅 목록이 없습니다.",
                        color = ColorUtils.gray_9B9A99,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listChat) { index, obj ->
                    if (obj.target?.id == user.id) {
                        ItemGeneralMember(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_CHAT_DETAIL,
                                ChatDetailFragment.newInstance(roomId = obj.id!!)
                            )
                        }
                    } else {
                        ItemCompanyMember(obj) {
                            viewController?.pushFragment(
                                ScreenId.SCREEN_CHAT_DETAIL,
                                ChatDetailFragment.newInstance(roomId = obj.id!!)
                            )
                        }
                    }
                    Divider(color = ColorUtils.gray_E1E1E1)
                }
            }

            LoadmoreHandler(lazyListState) { page ->
                viewModel.getChatList(accessToken, page)
            }
        }
    }
}

@Composable
private fun ItemCompanyMember(obj: RoomDetailModel, onClick: () -> Unit?) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        GlideImage(
            imageModel = "${BuildConfig.BASE_S3}${""}",
            Modifier
                .size(80.dp),
            placeHolder = painterResource(R.drawable.ic_default_nagaja),
            error = painterResource(R.drawable.ic_default_nagaja)
        )

        Column(
            Modifier
                .height(80.dp)
                .weight(1f)
                .padding(9.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "${obj.target?.name}",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "상담중",
                    color = ColorUtils.blue_2177E4,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 9.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "${obj.lastMessage?.message}",
                color = ColorUtils.black_000000,
                fontSize = 14.sp,
            )
        }

        Image(
            painterResource(R.drawable.ic_more),
            null,
            modifier = Modifier
                .padding(top = 15.dp)
                .noRippleClickable { })
    }
}

@Composable
private fun ItemGeneralMember(obj: RoomDetailModel, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        Column(
            Modifier
                .height(70.dp)
                .weight(1f)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    "${obj.actor?.name}",
                    color = ColorUtils.gray_222222,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "상담중",
                    color = ColorUtils.blue_2177E4,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 9.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "${obj.lastMessage?.message}",
                color = ColorUtils.black_000000,
                fontSize = 14.sp,
            )
        }

        Image(
            painterResource(R.drawable.ic_more),
            null,
            modifier = Modifier
                .padding(top = 15.dp)
                .noRippleClickable { })
    }
}