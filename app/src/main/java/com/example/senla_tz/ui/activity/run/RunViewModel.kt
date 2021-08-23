package com.example.senla_tz.ui.activity.run

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.service.TrackLocationWorker
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
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

    @SuppressLint("MissingPermission")
    fun startTrackLocation(context: Context) {
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
    }

    fun updateState(latLng: LatLng? = null){
        repository.updateState(latLng)
    }

    override fun onCleared() {
        super.onCleared()
    }

}