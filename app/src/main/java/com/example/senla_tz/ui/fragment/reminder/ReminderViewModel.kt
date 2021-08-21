package com.example.senla_tz.ui.fragment.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository
): ViewModel() {

    val remindersFlow: SharedFlow<MutableList<Reminder>> by lazy { repository.remindersFlow }
    val reminderFlow: SharedFlow<Reminder> by lazy { repository.reminderFlow }

    fun getAllReminder(){
        viewModelScope.launch {
            repository.getAllReminder()
        }
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
    }

    fun deleteReminderById(id: Int){
        viewModelScope.launch {
            repository.deleteReminderById(id)
        }
    }
}