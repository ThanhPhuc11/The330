package com.nagaja.the330.view.signupinfo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.nagaja.the330.MainActivity
import com.nagaja.the330.R
import com.nagaja.the330.base.BaseFragment
import com.nagaja.the330.utils.ColorUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable

class GoogleMapFragment : BaseFragment() {
    var callbackLocation: ((Double, Double) -> Unit)? = null

    companion object {
        fun newInstance() = GoogleMapFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        val singapore = LatLng(21.043256, 105.858470)
        val lat = remember { mutableStateOf(singapore.latitude) }
        val long = remember { mutableStateOf(singapore.longitude) }
        val cameraPositionState: CameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(lat.value, long.value), 15f)
        }
        LayoutTheme330 {
            Header("") {
                viewController?.popFragment()
            }
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onMapClick = {
                    Log.e("location", it.latitude.toString() + ";" + it.longitude.toString())
                    lat.value = it.latitude
                    long.value = it.longitude
                }
            ) {
                Marker(position = LatLng(lat.value, long.value), title = "")
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ColorUtils.blue_2177E4)
                    .noRippleClickable {
                        callbackLocation?.invoke(lat.value, long.value)
                        viewController?.popFragment()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.confirm),
                    color = ColorUtils.white_FFFFFF,
                    fontSize = 18.sp
                )
            }
        }
    }
}