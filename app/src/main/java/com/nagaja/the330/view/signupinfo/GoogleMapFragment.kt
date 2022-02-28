package com.nagaja.the330.view.signupinfo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
import com.nagaja.the330.utils.CommonUtils
import com.nagaja.the330.view.Header
import com.nagaja.the330.view.LayoutTheme330
import com.nagaja.the330.view.noRippleClickable

class GoogleMapFragment : BaseFragment() {
    var callbackLocation: ((Double, Double) -> Unit)? = null

    var callbackMyLocation: ((Double, Double) -> Unit)? = null

    companion object {
        fun newInstance() = GoogleMapFragment()
    }

    override fun SetupViewModel() {
        viewController = (activity as MainActivity).viewController
    }

    @Composable
    override fun UIData() {
        val owner = LocalLifecycleOwner.current
        val seoul = LatLng(37.566536, 126.977966)
        val lat = remember { mutableStateOf(seoul.latitude) }
        val long = remember { mutableStateOf(seoul.longitude) }
        val cameraPositionState: CameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(lat.value, long.value), 15f)
        }
        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        callbackMyLocation = { myLat, myLong ->
                            lat.value = myLat
                            long.value = myLong
                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(LatLng(lat.value, long.value), 15f)
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!CommonUtils.hasPermissions(
                                    context,
                                    CommonUtils.locationPermission
                                )
                            ) {
                                callbackPermissionLocation.launch(CommonUtils.locationPermission)
                            } else {
                                getLocation()
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
            Header("Google Map") {
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

    private val callbackPermissionLocation =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.any { it.value }) {
                getLocation()
            }
        }

    private fun getLocation() {
        val location = getLastKnownLocation()
        val longitude = location?.longitude ?: 0.0
        val latitude = location?.latitude ?: 0.0
        callbackMyLocation?.invoke(latitude, longitude)
        Log.e("PHUC", "$longitude : $latitude")
    }

    private fun getLastKnownLocation(): Location? {
        val mLocationManager: LocationManager = context?.getSystemService(
            AppCompatActivity.LOCATION_SERVICE
        ) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                break
            }
            val l: Location = mLocationManager.getLastKnownLocation(provider)
                ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }
}