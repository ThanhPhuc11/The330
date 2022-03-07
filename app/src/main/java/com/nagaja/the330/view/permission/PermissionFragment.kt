package com.nagaja.the330.view.permission

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.data.DataStorePref
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.utils.CommonUtils
import com.nagaja.the330.utils.ScreenId
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.login.LoginFragment
import com.nagaja.the330.view.noRippleClickable

class PermissionFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance() = PermissionFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        PreviewUI()
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!CommonUtils.hasPermissions(context, CommonUtils.askFirstAppPermission)) {
                callbackPermission.launch(CommonUtils.askFirstAppPermission)
//                return
            }
            DataStorePref(requireContext()).setFirst(false)
            //TODO: next screen
            viewController?.pushFragment(ScreenId.SCREEN_LOGIN, LoginFragment.newInstance())
        }
    }

    @Preview
    @Composable
    fun PreviewUI() {
        LayoutTheme330 {
            Column(
                Modifier
                    .padding(dimensionResource(R.dimen.dp_16))
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    stringResource(R.string.app_access_permission_guide),
                    fontSize = 24.sp,
                    color = ColorUtils.gray_222222,
                    modifier = Modifier.paddingFromBaseline(top = dimensionResource(R.dimen.dp_80))
                )
                Text(
                    stringResource(R.string.body_content_permission),
                    color = ColorUtils.gray_626262,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.dp_12))
                )
                ConstraintLayout(Modifier.padding(top = 30.dp)) {
                    val (image, text1, text2) = createRefs()
                    Image(
                        painter = painterResource(R.drawable.ic_mark_permission),
                        contentDescription = "",
                        Modifier.constrainAs(image) {
                            top.linkTo(parent.top)
                        }
                    )
                    Text(
                        stringResource(R.string.map),
                        color = ColorUtils.gray_222222,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(text1) {
                            top.linkTo(image.top)
                            bottom.linkTo(image.bottom)
                            start.linkTo(image.end, margin = 8.dp)
                        })

                    Text(
                        stringResource(R.string.used_to_recomment_near_bussiness),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(text2) {
                            top.linkTo(text1.bottom)
                            start.linkTo(text1.start)
                        })
                }
                ConstraintLayout(Modifier.padding(top = 16.dp)) {
                    val (image, text1, text2) = createRefs()
                    Image(
                        painter = painterResource(R.drawable.ic_camera_permission),
                        contentDescription = "",
                        Modifier.constrainAs(image) {
                            top.linkTo(parent.top)
                        }
                    )
                    Text(
                        stringResource(R.string.camera),
                        color = ColorUtils.gray_222222,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(text1) {
                            top.linkTo(image.top)
                            bottom.linkTo(image.bottom)
                            start.linkTo(image.end, margin = 8.dp)
                        })

                    Text(
                        stringResource(R.string.upload_photo_and_video),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(text2) {
                            top.linkTo(text1.bottom)
                            start.linkTo(text1.start)
                        })
                }

                Spacer(modifier = Modifier.weight(1f))
                Box(
                    Modifier
                        .padding(vertical = 16.dp)
                        .background(
                            color = ColorUtils.gray_E1E1E1
                        )
                        .height(1.dp)
                        .fillMaxWidth()
                )
                ConstraintLayout {
                    val (dot1, dot2, dot3, text1, text11, text21, text2, text3) = createRefs()
                    Image(
                        painterResource(id = R.drawable.ic_dot), contentDescription = "",
                        Modifier.constrainAs(dot1) {
                            start.linkTo(parent.start)
                            top.linkTo(text1.top)
                            bottom.linkTo(text1.bottom)
                        }
                    )
                    Text(
                        stringResource(R.string.how_to_change_access_right),
                        color = ColorUtils.gray_626262,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text1) {
                            top.linkTo(parent.top)
                            start.linkTo(dot1.end, 5.dp)
                        }
                    )

                    Text(
                        stringResource(R.string.setting_on_off),
                        color = ColorUtils.gray_9F9F9F,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text11) {
                            top.linkTo(text1.bottom)
                            start.linkTo(text1.start)
                        }
                    )

                    Image(
                        painterResource(id = R.drawable.ic_dot), contentDescription = "",
                        Modifier.constrainAs(dot2) {
                            start.linkTo(parent.start)
                            top.linkTo(text21.top)
                            bottom.linkTo(text21.bottom)
                        }
                    )
                    Text(
                        "권",
                        color = ColorUtils.gray_626262,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text21) {
                            top.linkTo(text11.bottom)
                            start.linkTo(text1.start)
                        }
                    )
                    Text(
                        stringResource(R.string.if_deny_app_may_not_working),
                        color = ColorUtils.gray_626262,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text2) {
                            top.linkTo(text11.bottom)
                            start.linkTo(text1.start)
                        }
                    )

                    Image(
                        painterResource(id = R.drawable.ic_dot), contentDescription = "",
                        Modifier.constrainAs(dot3) {
                            start.linkTo(parent.start)
                            top.linkTo(text3.top)
                            bottom.linkTo(text3.bottom)
                        }
                    )
                    Text(
                        stringResource(R.string.after_deny_you_can_allow_in_the_setting),
                        color = ColorUtils.gray_626262,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(text3) {
                            top.linkTo(text2.bottom)
                            start.linkTo(dot1.end, 5.dp)
                        }
                    )
                }
            }
            Box(
                Modifier
                    .paddingFromBaseline(bottom = 0.dp)
                    .padding(top = 44.dp, bottom = 34.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {
                        askPermission()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.confirm),
                    fontSize = 18.sp,
                    color = ColorUtils.white_FFFFFF,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}