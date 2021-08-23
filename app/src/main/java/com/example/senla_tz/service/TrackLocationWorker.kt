package com.example.senla_tz.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import androidx.work.impl.utils.futures.SettableFuture
import com.example.senla_tz.repository.RunRepository
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.common.util.concurrent.ListenableFuture
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

private val TAG = TrackLocationWorker::class.java.simpleName
private const val LOCATION_WORK_TAG = "LOCATION_WORK_TAG"
@HiltWorker
class TrackLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: RunRepository
) : ListenableWorker (context, workerParams) {

    private lateinit var mFuture: SettableFuture<Result>

    @SuppressLint("RestrictedApi", "MissingPermission")
    override fun startWork(): ListenableFuture<Result> {
        mFuture = SettableFuture.create()

        LocationServices.getFusedLocationProviderClient(context).apply {
            requestLocationUpdates( LocationRequest.create().apply {
                interval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                isWaitForAccurateLocation = true
                maxWaitTime = 3000
            },object : LocationCallback(){
                override fun onLocationResult(result: LocationResult?) {
                    Log.e(TAG,"init ")
                    if (result == null) return
                    repository.emitLocationFlow(result.locations)
                    removeLocationUpdates(this)
                }
            }, looper)
        }

        val locationWorker =
                OneTimeWorkRequestBuilder<TrackLocationWorker>()
                    .addTag(LOCATION_WORK_TAG)
                    .setInitialDelay(10,TimeUnit.SECONDS)
                    .build()
            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    LOCATION_WORK_TAG,
                    ExistingWorkPolicy.REPLACE,
                    locationWorker
                )

            mFuture.set(Result.success())

        return mFuture
    }

    override fun onStopped() {

        Log.e(TAG,"onStopped")
        WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_WORK_TAG)
        super.onStopped()
    }


}