package com.nagaja.the330.view.event

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.BannerCompanyModel
import com.nagaja.the330.ui.theme.textBold18
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.AppDateUtils
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.ColorUtils.gray_00000D
import com.nagaja.the330.utils.ColorUtils.white_FFFFFF
import com.nagaja.the330.utils.LoadmoreHandler
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330

class OnGoingEventsFragment : BaseFragment() {
    private lateinit var viewModel: EventVM

    companion object {
        fun newInstance() = OnGoingEventsFragment()
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[EventVM::class.java]
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
                        accessToken?.let {
                            viewModel.getEvent(accessToken!!, 0)
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

        LayoutTheme330 {
            Column {
                Header(stringResource(R.string.title_on_going_events)) {
                    viewController?.popFragment()
                }
                Box(
                    Modifier.background(white_FFFFFF)
                ) {
                    val listData = viewModel.stateListEvent
                    val lazyListState = rememberLazyListState()
                    LazyColumn(state = lazyListState) {
                        items(items = listData) { item ->
                            ItemEvent(event = item)
                        }
                    }
                    LoadmoreHandler(lazyListState) { page ->
                        viewModel.getEvent(accessToken!!, page)
                    }
                }
            }
        }
    }
}

@Composable
private fun EventList(list: SnapshotStateList<BannerCompanyModel>) {

}

@Composable
private fun ItemEvent(event: BannerCompanyModel) {
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
private fun CardContent(event: BannerCompanyModel) {
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
                Modifier.padding(top = 5.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppDateUtils.changeDateFormat(
                        AppDateUtils.FORMAT_16,
                        AppDateUtils.FORMAT_15,
                        event.createdOn ?: ""
                    ),
                    style = textRegular12
                )
                Image(
                    painter = painterResource(R.drawable.ic_dot),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(2.dp),
                    colorFilter = ColorFilter.tint(ColorUtils.gray_9F9F9F)
                )
                Text(
                    text = "조회수 ${event.viewCount ?: 0}",
                    style = textRegular12
                )
            }
            if (expanded) {
                Column {
                    Text(
                        text = HtmlCompat.fromHtml(
                            event.body ?: "",
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString(),
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