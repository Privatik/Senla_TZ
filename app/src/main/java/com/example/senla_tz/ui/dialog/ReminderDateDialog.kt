package com.example.senla_tz.ui.dialog

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

        fun newInstance(isFirstStart: Boolean): ReminderDateDialog =
            ReminderDateDialog().apply {
                arguments = bundleOf(IS_FIRST_START to isFirstStart)
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogReminderDateBinding.bind(view)

        binding?.apply {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR_OF_DAY,2)

            npDay.setValue(
                min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH),
                max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH),
                current = calendar.get(Calendar.DAY_OF_MONTH)
            )

            npMonth.setValue(
                min = calendar.get(Calendar.MONTH) + 1,
                max = if (calendar.get(Calendar.MONTH) != 11) calendar.get(Calendar.MONTH) + 2
                      else                                    1,
                current = calendar.get(Calendar.MONTH) + 1
            )

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
                    day = npDay.value,
                    month = npMonth.value - 1
                )

                callback?.createReminder(calendar = calendar)
                dismiss()
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