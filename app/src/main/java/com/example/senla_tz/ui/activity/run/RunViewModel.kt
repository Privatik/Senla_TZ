package com.example.senla_tz.ui.activity.run

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.service.TrackLocationWorker
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"
@HiltViewModel
class RunViewModel @Inject constructor(
    private val repository: RunRepository
): ViewModel() {

    val locationFlow: SharedFlow<Location> by lazy {
        repository.locationFlow
    }

    fun loadSetup(context: Context){
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(
                        LocationRequest.create().apply {
                            interval = 5000
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            maxWaitTime= 1000
                        }
                    )
                    .setAlwaysShow(true)
                    .build()
            )

    }

    fun startTrackLocation(context: Context) {
        val locationWorker =
            PeriodicWorkRequestBuilder<TrackLocationWorker>(
                15, TimeUnit.SECONDS)
                .addTag(LOCATION_WORK_TAG)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                LOCATION_WORK_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                locationWorker
            )
    }

    fun stopTrackLocation(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_WORK_TAG)
    }

}