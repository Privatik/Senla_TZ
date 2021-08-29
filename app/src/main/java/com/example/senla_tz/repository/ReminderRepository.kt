package com.example.senla_tz.repository

import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.repository.database.ReminderDao
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val dao: ReminderDao
) {

    val remindersFlow = MutableSharedFlow<MutableList<Reminder>>()
    val reminderFlow = MutableSharedFlow<Reminder>()

    suspend fun getAllReminder(){
       remindersFlow.emit(dao.getActualAllReminder(Calendar.getInstance()).sortedBy { it.date.timeInMillis }.toMutableList())
    }

    suspend fun saveReminder(reminder: Reminder) {
        val id = dao.saveReminder(reminder).toInt()
        reminderFlow.emit(reminder.copy(id = id))
    }

    suspend fun updateReminder(reminder: Reminder) {
        dao.saveReminder(reminder)
    }

    suspend fun deleteReminderById(id: Int) = dao.deleteReminderById(id)
}