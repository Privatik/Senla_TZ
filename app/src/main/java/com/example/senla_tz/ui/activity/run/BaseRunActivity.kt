package com.example.senla_tz.ui.activity.run

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
import com.example.senla_tz.service.RunService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*

private val TAG = BaseRunActivity::class.java.simpleName
@SuppressLint("MissingPermission")
open class BaseRunActivity: BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var isRunState = false

    val managerLocation: LocationManager by lazy {
        getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private val line : PolylineOptions by lazy {
        PolylineOptions().apply {
            width(5f)
            color(ContextCompat.getColor(this@BaseRunActivity, R.color.purple_700))
        }
    }

        private val bindConnect = object : ServiceConnection {
        private lateinit var currentMarker: Marker

        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as RunService.LocationBinder

            Log.e(TAG,"onConnectBInd")

            binder.updateLocationListener(object : RunService.LocationBinder.ChangeLatLng{
                override fun update(latLng: LatLng) {
                    if (isLoadState()) closeLoadDialog()

                    if (isRunState){
                        line.points.add(latLng)
                            mMap.addPolyline(line)
                    } else {
                        if (!(::currentMarker.isInitialized)) {
                            mMap.addMarker(MarkerOptions().position(latLng))?.let {
                                currentMarker = it
                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                        }
                        else currentMarker.position = latLng
                    }
                }
            })

            managerLocation.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000.toLong(),
                5.toFloat(),
                binder.listener)
        }

        override fun onServiceDisconnected(className: ComponentName) {

        }

        fun startRun(){
            currentMarker.apply {
                line.add(position)
                remove()
            }
        }

        fun finishRun(){
            line.points.apply {
                mMap.addMarker(MarkerOptions().position(last()))?.let {
                    currentMarker = it
                }
                clear()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openLoadDialog()

        val activityResultLauncherPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                if (!isGranted) {
                    (Objects.requireNonNull(this.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager)
                        .clearApplicationUserData()
                    recreate()
                }
            }
        }

        activityResultLauncherPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onResume() {
        super.onResume()
    }

    fun updateListenerLocation(isRunState: Boolean){
        if (::mMap.isInitialized){
            if (isRunState){
                bindConnect.startRun()
            } else{
                bindConnect.finishRun()
            }

            this.isRunState = isRunState
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG,"ready")
        mMap = googleMap
        mMap.addPolyline(line)

        bindService(
            Intent(this, RunService::class.java),
            bindConnect,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        unbindService(bindConnect)

        super.onDestroy()
    }


}