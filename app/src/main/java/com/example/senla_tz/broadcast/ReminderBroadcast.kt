package com.example.senla_tz.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.senla_tz.repository.database.ReminderDao
import com.example.senla_tz.service.ReminderNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = ReminderBroadcast::class.java.simpleName
@AndroidEntryPoint
class ReminderBroadcast: HiltReminderBroadcast() {

    @Inject lateinit var dao: ReminderDao

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @DelicateCoroutinesApi
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val intentService = Intent(context, ReminderNotificationService::class.java)

        intent.getIntExtra("id", 0).apply {
            Log.e(TAG,"reminder id = $this")
            if (this != 0) {
                GlobalScope.launch {dao.deleteReminderById(this@apply)}

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentService)
                }else {
                    context.startService(intentService)
                }
            }
        }
    }
}

abstract class HiltReminderBroadcast: BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {}

}