package com.example.senla_tz.ui.dialog.reminder

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.senla_tz.R
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.example.senla_tz.databinding.DialogReminderDateBinding
import com.example.senla_tz.service.ReminderNotificationService

import com.example.senla_tz.ui.activity.main.MainActivity
import com.example.senla_tz.util.extends.changeDate
import java.util.*


private val TAG = ReminderDateDialog::class.java.simpleName
class ReminderDateDialog: DialogFragment(R.layout.dialog_reminder_date) {

    private var binding: DialogReminderDateBinding? = null

    var callback: Callback? = null

    companion object{
        private const val IS_FIRST_START = "IsFirstStart"
        private const val DATE_REMINDER = "DateReminder"

        fun newInstance(isFirstStart: Boolean, calendar: Calendar): ReminderDateDialog =
            ReminderDateDialog().apply {
                arguments = bundleOf(
                    IS_FIRST_START to isFirstStart,
                    DATE_REMINDER to calendar
                )
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogReminderDateBinding.bind(view)

        binding?.apply {
            val adapterDayOfWeek = AdapterDayOfWeek()
            recDayOfWeek.adapter = adapterDayOfWeek

            (requireArguments().getSerializable(DATE_REMINDER) as? Calendar)?.let { calendar ->
                npHour.setValue(
                    min = calendar.getActualMinimum(Calendar.HOUR_OF_DAY),
                    max = calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
                    current = calendar.get(Calendar.HOUR_OF_DAY)
                )

                npMinute.setValue(
                    min = calendar.getActualMinimum(Calendar.MINUTE),
                    max = calendar.getActualMaximum(Calendar.MINUTE),
                    current = calendar.get(Calendar.MINUTE)
                )

                btnCreate.setOnClickListener {
                    calendar.changeDate(
                        hour = npHour.value,
                        minute = npMinute.value,
                        dayCount = adapterDayOfWeek.currentDay
                    )

                    callback?.createReminder(calendar = calendar)
                    dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dismiss()
            }

            if (!requireArguments().getBoolean(IS_FIRST_START)) {
                btnCreate.text = getString(R.string.edit)

                btnDelete.visibility = View.VISIBLE
                btnDelete.setOnClickListener {
                    callback?.deleteReminder()
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun NumberPicker.setValue(min: Int, max: Int, current: Int){
        minValue = min
        maxValue = max
        value = current
    }

    interface Callback{
        fun createReminder(calendar: Calendar)
        fun deleteReminder()
    }
}