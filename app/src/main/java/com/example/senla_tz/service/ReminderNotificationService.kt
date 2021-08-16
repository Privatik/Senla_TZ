package com.example.senla_tz.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.example.senla_tz.R

private const val channelId = "Reminder Channel"
private const val channelName = "Reminder Name"
private const val TEXT_RUN = "Пора на пробежку"
private const val REMINDER = "Напоминание"
private val TAG = ReminderNotificationService::class.java.simpleName
class ReminderNotificationService: Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        Log.e(TAG,"onStart")
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(service)
            } else {
               ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.planet)
            .setContentTitle(TEXT_RUN)
            .setSubText(REMINDER)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        service.notify(101,notification)
        //startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(service: NotificationManager): String{
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).also {
            it.lightColor = getColor(R.color.purple_700)
            it.importance = NotificationManager.IMPORTANCE_NONE
            it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            service.createNotificationChannel(it)
        }
        return channelId
    }
}