package com.example.senla_tz.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.example.senla_tz.R
import com.example.senla_tz.ui.activity.run.RunActivity
import com.example.senla_tz.util.Constant.FROM_SERVICE

private const val channelId = "Reminder Channel"
private const val channelName = "Reminder Name"
private const val TEXT_RUN = "Пора на пробежку"
private const val REMINDER = "Напоминание"
private val TAG = ReminderNotificationService::class.java.simpleName
class ReminderNotificationService: Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG,"${intent.action}")
        if (intent.action != null && intent.action == FROM_SERVICE){
            stopForeground(true)
            stopSelf()
        } else{
            startForeground()
        }
        Log.e(TAG,"onStart")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? = null

//    override fun onHandleWork(intent: Intent) {
//        startForeground()
//        Log.e(TAG,"onStart")
//    }

    private fun startForeground() {
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(service)
            } else {
               ""
            }

        val intent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, RunActivity::class.java).putExtra(FROM_SERVICE,true),
            0
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.planet)
            .setContentTitle(TEXT_RUN)
            .setSubText(REMINDER)
            .setPriority(PRIORITY_HIGH)
            .setContentIntent(intent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        //service.notify(101,notification)
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(service: NotificationManager): String{
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).also {
            it.lightColor = getColor(R.color.purple_700)
            it.importance = NotificationManager.IMPORTANCE_NONE
            it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            it.enableVibration(true)
            service.createNotificationChannel(it)
        }
        return channelId
    }
}