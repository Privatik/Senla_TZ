package com.example.senla_tz.repository

import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.repository.database.ReminderDao
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val dao: ReminderDao
) {

    val remindersFlow = MutableSharedFlow<MutableList<Reminder>>()

    suspend fun getAllReminder(){
       remindersFlow.emit(dao.getAllReminder().sortedBy { it.date.timeInMillis }.toMutableList())
    }

    fun saveReminder(reminder: Reminder) = dao.saveReminder(reminder)
    suspend fun deleteReminderById(id: Int) = dao.deleteReminderById(id)
}