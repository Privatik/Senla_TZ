package com.example.senla_tz.controller

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.senla_tz.broadcast.ReminderBroadcast
import com.example.senla_tz.repository.database.ReminderDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private val TAG = NotificationController::class.java.simpleName
class NotificationController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: ReminderDao
    ) {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun deleteAllReminder(isDeleteReminderFromDataBase: Boolean = false){
        CoroutineScope(Dispatchers.Default).launch {
            val list = dao.getActualAllReminder(Calendar.getInstance())

            Log.e(TAG,"$list")
            list.forEach {
                deleteReminder(it.id)
            }

            if (isDeleteReminderFromDataBase){
                dao.deleteAll()
            }
        }
    }

    fun addAllReminder(){
        CoroutineScope(Dispatchers.Default).launch {
            val list = dao.getActualAllReminder(Calendar.getInstance())

            list.forEach {
                addReminder(it.id, it.date)
            }
        }
    }

    fun addReminder(id: Int, calendar: Calendar){
        val intent = PendingIntent.getBroadcast(
            context,
            id,
            Intent(context, ReminderBroadcast::class.java).putExtra("id",id),
            0
        )

        Log.d(TAG,calendar.time.toString())
        alarmManager.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intent
                )
            } else {
                setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intent
                )
            }
        }
    }

    fun updateReminder(id: Int, calendar: Calendar){
        deleteReminder(id)
        addReminder(id, calendar)
    }

    fun deleteReminder(id:Int){
        val intent = PendingIntent.getBroadcast(
            context,
            id,
            Intent(context, ReminderBroadcast::class.java),
            0
        )

        alarmManager.apply {
            cancel(intent)
        }
    }
}