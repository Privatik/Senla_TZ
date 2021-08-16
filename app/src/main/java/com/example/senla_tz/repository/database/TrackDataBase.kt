package com.example.senla_tz.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.entify.Track

@Database(entities = [Track::class, Reminder::class], version = 1, exportSchema = false)
abstract class TrackDataBase(): RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun reminderDao(): ReminderDao
}