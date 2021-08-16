package com.example.senla_tz.ui.fragment.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.databinding.FragmentReminderBinding
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.service.ReminderNotificationService
import com.example.senla_tz.ui.activity.main.IMainNavController
import com.example.senla_tz.ui.dialog.ReminderDateDialog
import com.example.senla_tz.util.resourse.RecyclerViewDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


private val TAG = ReminderFragment::class.simpleName
class ReminderFragment : Fragment(R.layout.fragment_reminder) {

    private var mainNavController: IMainNavController? = null
    private var binding: FragmentReminderBinding? = null

    private val vm: ReminderViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainNavController = context as IMainNavController
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentReminderBinding.bind(view)

        binding?.apply {
            mainNavController?.addNavController(toolbar = toolbar)

            recReminder.addItemDecoration(RecyclerViewDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.decoration_white)!!))
        }

        initListener()
        initObserver()

    }

    private fun initListener() {
        binding?.apply {
            fabAddReminder.setOnClickListener {
              showDialogReminder(true)
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.remindersFlow.collect {
                    binding?.recReminder?.adapter = AdapterReminder(it){ reminder ->
                        showDialogReminder(false, reminder.id)
                    }
                }
            }
        }
    }

    private fun showDialogReminder(isFirstStart: Boolean, id: Int? = null){
        val dialog = ReminderDateDialog.newInstance(isFirstStart)
        dialog.callback = object : ReminderDateDialog.Callback{
            override fun createReminder(calendar: Calendar) {
                if (id == null) {
                    addReminder(
                        calendar,
                        vm.saveReminder(
                            Reminder(
                                id = 0,
                                date = calendar
                            )
                        ).id)
                }else {
                    addReminder(calendar, id)
                    vm.saveReminder(Reminder(
                        id = id,
                        date = calendar
                    ))
                }
            }

            override fun deleteReminder() {
                id?.let {
                    deleteReminder(it)
                    vm.deleteReminderById(it)
                }
            }

        }

        dialog.show(childFragmentManager,ReminderDateDialog::javaClass.name)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun addReminder(calendar: Calendar, id: Int){
        val intent = PendingIntent.getService(
            requireContext().applicationContext,
            id,
            Intent(requireActivity(), ReminderNotificationService::class.java),
            0
        )

        Log.d(TAG,calendar.time.toString())
        (requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intent
                )
            } else {
                setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intent
                )
            }
        }
    }

    private fun deleteReminder(id: Int){
        val intent = PendingIntent.getService(
            requireContext().applicationContext,
            id,
            Intent(requireActivity(), ReminderNotificationService::class.java),
            0
        )

        (requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
           cancel(intent)
        }
    }

    override fun onDetach() {
        mainNavController = null
        super.onDetach()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
