package com.nagaja.the330.view.event

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.EventModel
import com.nagaja.the330.ui.theme.The330Theme
import com.nagaja.the330.ui.theme.textBold18
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ColorUtils.gray_00000D
import com.nagaja.the330.utils.ColorUtils.white_FFFFFF
import com.nagaja.the330.view.Header
import com.skydoves.landscapist.glide.GlideImage

class OnGoingEventsFragment : BaseFragment() {
    private lateinit var viewModel: EventViewModel

    companion object {
        fun newInstance() = OnGoingEventsFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[EventViewModel::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {  }
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }

        The330Theme{
            Column {
                Header(stringResource(R.string.title_notification)) {
                    viewController?.popFragment()
                }
                Box(
                    Modifier.background(white_FFFFFF)
                ){
                    val eventList: List<EventModel> = List(1000) {
                        EventModel().apply {
                            id = it
                            title = "나가자 할로윈 특가! $it"
                            content = ("이벤트 게시물 내용이 작성되는 페이지 입니다.\n").repeat(4)
                            viewCount = 100
                            createdOn = "2021.10.18"
                        }
                    }
                    EventList(list = eventList)
                }
            }
        }
    }
}

@Composable
private fun EventList(list: List<EventModel>) {
    LazyColumn(state = rememberLazyListState()){
        items(items = list) { item ->
            Event(event = item)
        }
    }
}

@Composable
private fun Event(event: EventModel) {
    Column(
        modifier = Modifier
            .background(white_FFFFFF)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        CardContent(event)
        Divider(
            Modifier
                .height(1.dp)
                .background(gray_00000D)
        )
    }
}

@Composable
private fun CardContent(event: EventModel) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = event.title as String,
                style = textBold18
            )
            Row(
                Modifier.padding(top = 5.dp, bottom = 16.dp)
            ) {
                Text(
                    text = event.createdOn as String,
                    style = textRegular12
                )
                Text(
                    text = "·",
                    style = textRegular12
                )
                Text(
                    text = event.viewCount.toString(),
                    style = textRegular12
                )
            }
            if (expanded) {
                Column {
                    Text(
                        text = event.content as String,
                        Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    "Show less"
                } else {
                    "Show more"
                }

            )
        }
    }
}