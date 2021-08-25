package com.example.senla_tz.ui.activity.run

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.service.TrackLocationWorker
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"
private val TAG = RunViewModel::class.java.simpleName
@HiltViewModel
class RunViewModel @Inject constructor(
    private val repository: RunRepository
): ViewModel() {

    val locationFlow: SharedFlow<List<LatLng>> by lazy {
        repository.locationFlow
    }

    val pointCurrentLineFlow: SharedFlow<List<LatLng>> by lazy {
        repository.pointCurrentFlow
    }

    val trackFailFlow: SharedFlow<String> by lazy {
        repository.trackFailFlow
    }

    @SuppressLint("MissingPermission")
    fun startTrackLocation(context: Context) {
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)

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
        WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_WORK_TAG)
        updateState()
    }

    fun updateState(latLng: LatLng? = null){
        repository.updateState(latLng)
    }

    fun saveTrack(track: Track){
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