package com.example.senla_tz.ui.fragment.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senla_tz.controller.NotificationController
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
    private val notificationController: NotificationController
): ViewModel() {

    val remindersFlow: SharedFlow<MutableList<Reminder>> by lazy { repository.remindersFlow }
    val reminderFlow: SharedFlow<Reminder> by lazy { repository.reminderFlow }

    fun getAllReminder(){
        viewModelScope.launch {
            repository.getAllReminder()
        }
    }

    fun addReminderNotify(reminder: Reminder){
        notificationController.addReminder(reminder.id, reminder.date)
    }

    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.saveReminder(reminder)
        }
    }
    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
        }
        notificationController.updateReminder(reminder.id, reminder.date)
    }

    fun deleteReminderById(id: Int){
        viewModelScope.launch {
            repository.deleteReminderById(id)
        }
        notificationController.deleteReminder(id)
    }
}