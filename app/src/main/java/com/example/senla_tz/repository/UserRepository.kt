package com.example.senla_tz.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.senla_tz.broadcast.ReminderBroadcast
import com.example.senla_tz.repository.database.ReminderDao
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.ui.activity.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val reminderDao: ReminderDao,
  private val trackDao: TrackDao,
  private val tokenPref: TokenPref
) {

    suspend fun exitCurrentUser(){
        val list = reminderDao.getAndDeleteAll()

        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        list.forEach {
            val intent = PendingIntent.getBroadcast(
                context,
                it.id,
                Intent(context, ReminderBroadcast::class.java),
                0
            )
            manager.cancel(intent)
        }

        trackDao.deleteAll()
        tokenPref.deleteToken()
    }
}