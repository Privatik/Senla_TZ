package com.example.senla_tz.ui.activity.run

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableLong
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.senla_tz.controller.NotificationController
import com.example.senla_tz.controller.RunController
import com.example.senla_tz.entify.Point
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.service.TrackLocationWorker
import com.example.senla_tz.util.extends.distance
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

private const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"
private val TAG = RunViewModel::class.java.simpleName
@HiltViewModel
class RunViewModel @Inject constructor(
    private val repository: RunRepository,
    private val notificationController: NotificationController,
    private val runController: RunController
): ViewModel() {

    val locationFlow: SharedFlow<List<LatLng>> by lazy {
        runController.locationFlow
    }

    val pointCurrentLineFlow: SharedFlow<List<LatLng>> by lazy {
        repository.pointCurrentFlow
    }

    val timerFlow: SharedFlow<Long> by lazy {
        runController.timerFlow
    }

    val trackFailFlow: SharedFlow<String> by lazy {
        repository.trackFailFlow
    }

    val distanceObserver = ObservableLong(0L)
    val timeObserver = ObservableLong(0L)

    fun startTimer(isStart: Boolean){
        runController.startForeground(isStart)
    }

    @SuppressLint("MissingPermission")
    fun startTrackLocation(context: Context) {
        notificationController.deleteAllReminder()

        val locationWorker =
            OneTimeWorkRequestBuilder<TrackLocationWorker>()
                .addTag(LOCATION_WORK_TAG)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                LOCATION_WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                locationWorker
            )
    }

    fun stopTrackLocation(context: Context) {
        Log.e(TAG,"cancel workManager")
        notificationController.addAllReminder()

        runController.startForeground(false)
        WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_WORK_TAG)
        updateState()

    }

    fun updateState(latLng: LatLng? = null){
        runController.updateState(latLng)
    }


    fun saveTrack(list: List<LatLng>){
        val milliseconds = timeObserver.get()

        val now = Calendar.getInstance().let {
            it.timeInMillis -= milliseconds
            it
        }


        val track = Track(
            id = 0L,
            idServer = Random.nextLong(),
            time = milliseconds,
            beginsAt = now.timeInMillis,
            distance = list.distance(),
            isHasService = false
        ).apply {
            points = list.map { Point(lat = it.latitude, lng = it.longitude) }
        }


        viewModelScope.launch {
            repository.saveTrack(track)
        }
    }

    fun getPoints(id: Long){
        viewModelScope.launch {
            repository.getPoints(id)
        }
    }

}