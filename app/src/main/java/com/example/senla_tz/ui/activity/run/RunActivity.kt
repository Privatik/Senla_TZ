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
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
import com.example.senla_tz.databinding.ActivityRunBinding
import com.example.senla_tz.entify.Point
import com.example.senla_tz.entify.Track
import com.example.senla_tz.service.ReminderNotificationService
import com.example.senla_tz.service.TimerBinder
import com.example.senla_tz.service.TimerService
import com.example.senla_tz.util.Constant.FROM_SERVICE
import com.example.senla_tz.util.Constant.TRACK
import com.example.senla_tz.util.Constant.VIEW_OLD_TRACK
import com.example.senla_tz.util.extends.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.random.Random


private val TAG = RunActivity::class.java.simpleName
private const val ZOOM_CAMERA = 17f
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

    private val timer: Timer by lazy { Timer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        openLoadDialog()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        checkPermission {
            mapFragment.getMapAsync(this)
        }

        if (intent.getBooleanExtra(FROM_SERVICE, false)) {
            val intent = Intent(this, ReminderNotificationService::class.java)
            intent.action = FROM_SERVICE
            startService(intent)
        } else if (intent.getBooleanExtra(VIEW_OLD_TRACK, false)) {
            intent.getParcelableExtra<Track>(TRACK)?.let {
                binding?.apply {
                    btnStartOrFinish.setVisible(false)
                    tvTimer.text = it.parseInTime(true)
                    tvDistance.text = it.parseInDistance()
                    binding?.toolbarRunContainer?.setVisible(true)

                    vm.getPoints(it.idServer)
                }

            }
        }

        initListener()
        initObserver()
    }

    private fun checkPermission(hasPermission: () -> Unit){
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
                hasPermission()
            }else {
                onBackPressed()
            }
        }

        if (!(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)){
            activityResultLauncherActivity.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }else{
            hasPermission()
        }
    }

    private fun initListener() {
        binding?.apply {
            btnStartOrFinish.setOnClickListener {
                isRunState = !isRunState
                val text = if (!isRunState) getString(R.string.start) else getString(R.string.finish)

                btnStartOrFinish.flipDownAnimation(text) {
                    if (isRunState) binding?.toolbarRunContainer?.setVisible(true)
                }

                updateStateActivity()
            }
        }
    }

    private fun saveTrack(list: List<LatLng>){
        val now = Calendar.getInstance().let {
            it.timeInMillis -= timer.milliseconds
            it
        }

        val track = Track(
            id = 0L,
            idServer = Random.nextLong(),
            time = timer.milliseconds,
            beginsAt = now.timeInMillis,
            distance = list.distance(),
            isHasService = false
        ).apply {
            points = list.map { Point(lat = it.latitude, lng = it.longitude) }
        }

        vm.saveTrack(track)
    }

    private fun updateStateActivity(){
        if (isRunState){
            binding?.toolbarRunContainer?.setVisible(true)

            val intentService = Intent(this, TimerService::class.java)

            val connection = object : ServiceConnection{
                override fun onServiceConnected(p0: ComponentName, binder: IBinder) {
                    (binder as TimerBinder).apply {
                        startTimer {
                            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                                binding?.tvTimer?.text = milliseconds.returnTimeFromMilliseconds()
                            }
                        }
                    }
                }

                override fun onServiceDisconnected(p0: ComponentName) {

                }

            }

            bindService(intentService,connection, BIND_IMPORTANT)

            timer.startTimer { milliseconds ->
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    binding?.tvTimer?.text = milliseconds.returnTimeFromMilliseconds()
                }
            }

            currentMarker?.also {
                vm.updateState(it.position)
                it.remove()
            }

        } else {
            timer.stopTimer()
            unbindService()

            line.points.apply {
                vm.updateState(last())
                finishLine(this)
                saveTrack(this)
            }

            vm.stopTrackLocation(this)
            binding?.btnStartOrFinish?.setVisible(false)
        }
    }

    private fun finishLine(list: List<LatLng>, isDrawLine: Boolean = false) {
        if (isDrawLine){
            line.points.clear()
            line.addAll(list)

            mMap.addPolyline(line)
        }

        mMap.addMarker(MarkerOptions()
            .title(getString(R.string.start))
            .position(list.first())
            .icon(bitmapDescriptorFromVector(R.drawable.point_start))
        )

        mMap.addMarker(MarkerOptions()
            .title(getString(R.string.finish))
            .position(list.last())
            .icon(bitmapDescriptorFromVector(R.drawable.point_finish))
        )

    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.locationFlow.collect{ listLatLng ->
                    if (!::mMap.isInitialized) return@collect

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
                                    .title(getString(R.string.me))
                                    .position(latLng)
                                    .icon(bitmapDescriptorFromVector(R.drawable.point_me))
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.pointCurrentLineFlow.collect {
                    if (isLoadState()) closeLoadDialog()
                    finishLine(it, true)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.trackFailFlow.collect {
                    binding?.root?.let { it1 -> showSnackBar(it1, it) }
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
        if (isRunState){
            binding?.root?.let { showSnackBar(it,getString(R.string.have_to_finish_track)) }
        }else{
            super.onBackPressed()
        }
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