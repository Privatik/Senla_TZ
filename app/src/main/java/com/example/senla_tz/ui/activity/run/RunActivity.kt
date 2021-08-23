package com.example.senla_tz.ui.activity.run

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.senla_tz.util.Constant.ME
import com.example.senla_tz.util.Constant.ZOOM_CAMERA
import com.example.senla_tz.util.extends.distance
import com.example.senla_tz.util.extends.flipDownAnimation
import com.example.senla_tz.util.extends.normalView
import com.example.senla_tz.util.extends.setVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*


private val TAG = RunActivity::class.java.simpleName
@AndroidEntryPoint
class RunActivity : BaseActivity(), OnMapReadyCallback {

    private var binding: ActivityRunBinding? = null

    private val vm: RunViewModel by viewModels()

    private lateinit var mMap: GoogleMap

    private var isRunState = false

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

        if (!(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)){
            activityResultLauncherActivity.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }else{
            mapFragment.getMapAsync(this)
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        var timer: Job? = null

        binding?.apply {
            btnStartOrFinish.setOnClickListener {
                isRunState = !isRunState
                val text = if (!isRunState) getString(R.string.start) else getString(R.string.finish)

                btnStartOrFinish.flipDownAnimation(text) {
                    //toolbarRunContainer.setVisible(isRunState)
                }

                timer = if (isRunState){
                    startTimer()
                } else {
                    timer?.let { stopTimer(it) }
                    null
                }
                updateListenerLocation()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timerView(milliSeconds: Long){

        val minutes = milliSeconds / 60000
        val seconds = milliSeconds - minutes * 60000
        binding?.tvTimer?.text = "" +
                minutes.normalView() +
                ":${seconds.normalView()}" +
                ":${milliSeconds.toString().takeLast(3).dropLast(1)}"
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer(): Job =
        CoroutineScope(Dispatchers.Default).launch {
            var milliSeconds = 0L

            // 1000  - 00:01:00
            // 1100  - 00:01:10

            // 67000 - 01:07:00
            while (true) {
                delay(10)
                milliSeconds += 10

                timerView(milliSeconds)
            }
        }

    private fun stopTimer(job: Job)  {
        lifecycleScope.launch {
            job.cancelAndJoin()
        }
    }

    private fun updateListenerLocation(){
        if (isRunState){
            currentMarker?.also {
                vm.updateState(it.position)
                it.remove()
            }
        } else {
            line.points.apply {
                mMap.addMarker(MarkerOptions().position(last()))?.let {
                    currentMarker = it
                }
                vm.updateState()
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.locationFlow.collect{ listLatLng ->
                    Log.e(TAG,"Draw on activity $listLatLng")

                    if (isLoadState()) closeLoadDialog()

                    if (isRunState) {
                        line.points.clear()
                        line.addAll(listLatLng)
                        updateStateRunning(line.points.distance())

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.last(), ZOOM_CAMERA))
                        mMap.addPolyline(line)
                    } else {
                        listLatLng.last().also { latLng ->
                            if (currentMarker == null) {
                                mMap.addMarker(MarkerOptions()
                                    .title(ME)
                                    .position(latLng)
                                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_me))
                                )?.let { marker ->
                                    currentMarker = marker
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA))
                            } else {
                                currentMarker?.position = latLng
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA))
                            }
                        }
                    }

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateStateRunning(distance: Int){
        binding?.apply {
            tvDistance.text = "$distance м"
        }
    }

    override fun onBackPressed() {
        vm.stopTrackLocation(this)
        super.onBackPressed()
    }

    override fun onDestroy() {
        binding = null
        vm.stopTrackLocation(this)
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        vm.startTrackLocation(this)
    }
}