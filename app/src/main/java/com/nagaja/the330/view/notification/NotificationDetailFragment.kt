package com.nagaja.the330.view.notification

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.GetDummyData
import com.nagaja.the330.model.NotificationModel
import com.nagaja.the330.ui.theme.textBold18
import com.nagaja.the330.ui.theme.textRegular12
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.skydoves.landscapist.glide.GlideImage

class NotificationDetailFragment: BaseFragment() {
    private lateinit var viewModel: NotificationVM

    companion object {
        fun newInstance(arg: Int) = NotificationDetailFragment().apply {
            arguments = Bundle().apply {
                this.putInt(AppConstants.EXTRA_KEY1, arg)
            }
        }
    }

    override fun SetupViewModel() {
        viewModel = getViewModelProvider(this)[NotificationVM::class.java]
        viewController = (activity as MainActivity).viewController
    }

    @Preview
    @Composable
    override fun UIData() {
        val noticeId = arguments?.getInt(AppConstants.EXTRA_KEY1)
        val owner = LocalLifecycleOwner.current

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        accessToken?.let {
                            noticeId?.let { it1 ->

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
        LayoutTheme330 {
            Header(stringResource(R.string.title_notification)) {
                viewController?.popFragment()
            }
            //TODO
            // Body(content = viewModel.noticeDetail)
            Body(content = GetDummyData.getNotificationDetail())
        }
    }
}

@Composable
fun Body(content: MutableState<NotificationModel>) {
    Column(
        Modifier
            .background(ColorUtils.white_FFFFFF)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text(
            text = "${content.value.question}",
            Modifier
                .padding(top = 16.dp),
            style = textBold18
        )
        Row(
            Modifier
                .padding(top = 6.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = content.value.createdOn ?: "",
                style = textRegular12
            )
            Text(
                text = "·",
                style = textRegular12
            )
            Text(
                text = "조회수 ${content.value.viewCount}",
                style = textRegular12
            )
        }
        Divider(
            Modifier
                .height(1.dp)
                .background(ColorUtils.gray_00000D)
        )
        GlideImage(
            imageModel = content.value.image,
            contentDescription = "",
            placeHolder = painterResource(R.drawable.img_dummy),
            error = painterResource(R.drawable.img_dummy),
            requestOptions = {
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            },
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Row {
            Text(
                text = HtmlCompat.fromHtml(
                    content.value.answer ?: "",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ).toString().replace("\n", ""),
                Modifier.align(Alignment.Bottom),
                style = textRegular12
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    Column {
        Header(stringResource(R.string.title_notification))
        Body(content = GetDummyData.getNotificationDetail())
    }
}
