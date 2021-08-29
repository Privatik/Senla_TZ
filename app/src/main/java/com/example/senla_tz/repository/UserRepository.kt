package com.example.senla_tz.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.senla_tz.broadcast.ReminderBroadcast
import com.example.senla_tz.controller.NotificationController
import com.example.senla_tz.repository.database.ReminderDao
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.ui.activity.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private val TAG = UserRepository::class.java.simpleName
class UserRepository @Inject constructor(
  private val reminderDao: ReminderDao,
  private val trackDao: TrackDao,
  private val tokenPref: TokenPref,
  private val notificationController: NotificationController
) {

    suspend fun exitCurrentUser(){
        Log.e(TAG,"DELETE ALL")
        notificationController.deleteAllReminder(true)

        trackDao.deleteAll()
        tokenPref.deleteToken()
    }
}