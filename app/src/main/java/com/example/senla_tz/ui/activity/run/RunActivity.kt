package com.example.senla_tz.ui.activity.run

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.senla_tz.R
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.base.BaseActivity
import com.example.senla_tz.databinding.ActivityRunBinding
import com.example.senla_tz.util.extends.flipDownAnimation
import com.example.senla_tz.util.extends.setVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


private val TAG = RunActivity::class.java.simpleName
@AndroidEntryPoint
class RunActivity : BaseActivity(), OnMapReadyCallback {

    private var binding: ActivityRunBinding? = null

    private val vm: RunViewModel by viewModels()

    private lateinit var mMap: GoogleMap

    private var isRunState = false

    private val managerLocation: LocationManager by lazy {
        getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private val line : PolylineOptions by lazy {
        PolylineOptions().apply {
            width(5f)
            color(ContextCompat.getColor(this@RunActivity, R.color.purple_700))
        }
    }

    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        openLoadDialog()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

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

        val activityResultLauncherActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                mapFragment.getMapAsync(this)
            }else {
                onBackPressed()
            }
        }

        if (!managerLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            activityResultLauncherActivity.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }else{
            mapFragment.getMapAsync(this)
        }

        initListener()
        initObserver()

        vm.loadSetup(this)
    }

    private fun initListener() {
        binding?.apply {
            btnStartOrFinish.setOnClickListener {
                isRunState = !isRunState
                val text = if (!isRunState) getString(R.string.start) else getString(R.string.finish)

                btnStartOrFinish.flipDownAnimation(text) {
                    tvTimer.setVisible(isRunState)
                }

                updateListenerLocation()
            }
        }
    }

    private fun updateListenerLocation(){
        if (isRunState){
            currentMarker?.also {
                line.add(it.position)
                it.remove()
            }
        } else {
            line.points.apply {
                mMap.addMarker(MarkerOptions().position(last()))?.let {
                    currentMarker = it
                }
                clear()
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                vm.locationFlow.collect{
                    Log.e(TAG,"$it")

                    if (isLoadState()) closeLoadDialog()
                    val latLng = LatLng(it.latitude, it.longitude)
                    if (isRunState) line.add(latLng)

                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        if (isRunState) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                            mMap.addPolyline(line)
                        } else {
                            if (currentMarker == null) {
                                mMap.addMarker(MarkerOptions().position(latLng))?.let { marker ->
                                    currentMarker = marker
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                            } else {
                                currentMarker?.position = latLng
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        vm.stopTrackLocation(this)
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        vm.startTrackLocation(this)
    }
}