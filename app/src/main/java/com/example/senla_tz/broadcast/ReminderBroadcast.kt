package com.example.senla_tz.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.senla_tz.service.ReminderNotificationService

class ReminderBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intentService = Intent(context, ReminderNotificationService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        }else {
            context.startService(intentService)
        }
    }
}