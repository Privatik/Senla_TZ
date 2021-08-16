package com.example.senla_tz.repository.database

import androidx.room.*
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.entify.Track

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    suspend fun getAllReminder(): MutableList<Reminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReminder(reminder: Reminder): Reminder

    @Query("DELETE FROM Reminder WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}