package com.example.senla_tz.ui.activity.run

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.base.BaseActivity
import com.example.senla_tz.databinding.ActivityRunBinding
import com.example.senla_tz.entify.Track
import com.example.senla_tz.service.ReminderNotificationService
import com.example.senla_tz.util.Constant.FROM_SERVICE
import com.example.senla_tz.util.Constant.FROM_WORKMANAGER
import com.example.senla_tz.util.Constant.TRACK
import com.example.senla_tz.util.Constant.VIEW_OLD_TRACK
import com.example.senla_tz.util.extends.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*

private val TAG = RunActivity::class.java.simpleName
@AndroidEntryPoint
class RunActivity : MapRunActivity() {

    private var binding: ActivityRunBinding? = null
    private val vm: RunViewModel by viewModels()

    private var isRunState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate")

        binding = ActivityRunBinding.inflate(layoutInflater).apply {
            setContentView(root)
            viewModel = vm
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        Log.e(TAG,"StopService")
        val intent = Intent(this, ReminderNotificationService::class.java)
        intent.action = FROM_SERVICE
        startService(intent)

        RunUtility.checkPermission(this) {
            openLoadDialog()

            mapFragment.getMapAsync(this)

            initListener()
            initObserver()
        }
    }

    private fun initListener() {
        binding?.apply {
            btnStartOrFinish.setOnClickListener {
                isRunState = !isRunState
                val text = if (!isRunState) getString(R.string.start) else getString(R.string.finish)

                btnStartOrFinish.flipDownAnimation(text) {
                    updateStateActivity()
                }
            }
        }
    }

    private fun updateStateActivity(){
        if (isRunState){
            binding?.toolbarRunContainer?.setVisible(true)

            vm.startTimer(true)

            currentMarker?.also {
                it.remove()
                vm.updateState(it.position)
                line.add(it.position)
            }

            Log.e(TAG,"Start run ${line.points}")

        } else {

            Log.e(TAG,"start - finish ${line.points}")
            line.points.apply {
                vm.updateState(last())
                finishLine(this)
                vm.saveTrack(this)
            }

            Log.e(TAG,"cancel workmanager")
            vm.stopTrackLocation(this)
            Log.e(TAG,"success work")
            binding?.btnStartOrFinish?.setVisible(false)
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.locationFlow.collect{ listLatLng ->
                    if (mMap == null) return@collect

                    Log.e(TAG,"Draw on activity $listLatLng")

                    if (isLoadState()) closeLoadDialog()

                    if (isRunState) {
                        updateTrack(listLatLng)
                    } else {
                        updateMyLocate(listLatLng.last())
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
                vm.timerFlow.collect {
                    vm.timeObserver.set(it)
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

    override fun updateTrack(list: List<LatLng>){
        super.updateTrack(list)
        vm.distanceObserver.set(line.points.distance())
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
        if (!isRunState) vm.stopTrackLocation(this)
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        when {
            intent.getBooleanExtra(VIEW_OLD_TRACK, false) -> {
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
            intent.getBooleanExtra(FROM_WORKMANAGER, false) -> {
                isRunState = true
                binding?.apply {
                    toolbarRunContainer.setVisible(true)
                    btnStartOrFinish.text = getString(R.string.finish)
                }
            }
            else -> {
                Log.e(TAG,"startLocationListener")
                vm.startTrackLocation(this)
            }
        }
    }
}