package com.example.senla_tz.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.impl.utils.futures.SettableFuture
import com.example.senla_tz.R
import com.example.senla_tz.controller.RunController
import com.example.senla_tz.ui.activity.run.RunActivity
import com.example.senla_tz.ui.activity.run.Timer
import com.example.senla_tz.util.Constant
import com.example.senla_tz.util.extends.returnTimeWithoutMilliSecondsFromMilliseconds
import com.google.android.gms.location.*
import com.google.common.util.concurrent.ListenableFuture
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


private const val channelId = "Run Channel"
private const val channelName = "Run Name"
private const val RUN = "Пробежка"
private const val NOTIFICATION_ID = 102
private val TAG = TrackLocationWorker::class.java.simpleName
@HiltWorker
class TrackLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val controller: RunController
) : ListenableWorker (context, workerParams) {

    private var channelIdFin: String? = null

    private lateinit var mFuture: SettableFuture<Result>
    private val timer: Timer by lazy { Timer() }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val fusedLocationProvider: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = 2000L
            smallestDisplacement = 5f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private val callback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.e(TAG, "new points $locationResult ${locationResult.locations}")
                locationResult.locations ?: return
                controller.emitLocationFlow(locationResult.lastLocation)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun stopWork(){
        Log.e(TAG,"stopWork")
        fusedLocationProvider.removeLocationUpdates(callback)
        timer.stopTimer()
        mFuture.set(Result.success())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getForegroundInfo(): ForegroundInfo {
        if (channelIdFin == null) {
            channelIdFin =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(notificationManager)
                } else {
                    ""
                }
        }

        val intent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, RunActivity::class.java).putExtra(Constant.FROM_WORKMANAGER,true),
            0
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelIdFin!!)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.planet)
            .setContentTitle("00:00")
            .setSubText(RUN)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)

        val foregroundInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            ForegroundInfo(NOTIFICATION_ID, notificationBuilder.build())
        }

        timer.startTimer {
            controller.emitTimer(it)
            notificationBuilder.setContentTitle(it.returnTimeWithoutMilliSecondsFromMilliseconds())
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }

        return foregroundInfo
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(service: NotificationManager): String{
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).also {
            it.lightColor = context.getColor(R.color.purple_700)
            it.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            service.createNotificationChannel(it)
        }
        return channelId
    }

    @SuppressLint("MissingPermission", "RestrictedApi")
    override fun startWork(): ListenableFuture<Result> {
        Log.e(TAG,"StartWork")
        mFuture = SettableFuture.create()
        try {
            controller.bindForeground { isStart ->
                if (isStart) {
                    setForegroundAsync(getForegroundInfo())
                } else {
                    stopWork()
                }
            }

            fusedLocationProvider.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())

        } catch (e: Throwable){
            mFuture.set(Result.failure())
        }
        return mFuture
    }
}