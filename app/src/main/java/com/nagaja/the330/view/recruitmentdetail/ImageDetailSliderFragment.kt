package com.nagaja.the330.view.recruitmentdetail

import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nagaja.the330.BuildConfig
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.model.FileModel
import com.nagaja.the330.utils.AppConstants
import com.nagaja.the330.utils.ColorUtils
import com.skydoves.landscapist.glide.GlideImage
import java.util.ArrayList

class ImageDetailSliderFragment : BaseFragment() {
    var listImage = mutableListOf<FileModel>()

    companion object {
        fun newInstance(images: MutableList<FileModel>) = ImageDetailSliderFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(AppConstants.EXTRA_KEY1, images as ArrayList<out Parcelable>)
            }
        }
    }
    override fun SetupViewModel() {

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        listImage = requireArguments().getParcelableArrayList(AppConstants.EXTRA_KEY1)!!
                    }
                    else -> {}
                }
            }
            owner.lifecycle.addObserver(observer)
            onDispose {
                owner.lifecycle.removeObserver(observer)
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(ColorUtils.black_000000),
            contentAlignment = Alignment.Center
        ) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            val pagerState = rememberPagerState(
                pageCount = listImage.size
            )
            HorizontalPager(state = pagerState) { page ->
                GlideImage(
                    imageModel = "${BuildConfig.BASE_S3}${
                        listImage.getOrNull(
                            page
                        )?.url
                    }",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenWidth.dp),
                    contentScale = ContentScale.Fit,
                    placeHolder = painterResource(R.drawable.ic_default_nagaja),
                    error = painterResource(R.drawable.ic_default_nagaja)
                )
            }
        }
    }
}