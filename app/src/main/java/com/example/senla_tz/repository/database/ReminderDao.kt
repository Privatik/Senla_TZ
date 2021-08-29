package com.example.senla_tz.repository.database

import android.util.Log
import androidx.room.*
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.entify.Track
import java.util.*

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    suspend fun getAllReminder(): List<Reminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: Reminder): Long

    @Query("DELETE FROM Reminder WHERE id = :id")
    suspend fun deleteReminderById(id: Int)

    @Query("DELETE FROM Reminder")
    suspend fun deleteAll()

    @Transaction
    suspend fun getActualAllReminder(calendar: Calendar):List<Reminder>{
        val list = getAllReminder()

        return list.filter{
            if (calendar.after(it.date)){
                Log.e("Dao","is calendar ${calendar.timeInMillis} > it ${it.date}")
                deleteReminderById(it.id)
                return@filter false
            }
            return@filter true
        }
    }

    @Transaction
    suspend fun getAndDeleteAll(): List<Reminder>{
        val list = getAllReminder()
        deleteAll()
        return list
    }
}