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
import com.example.senla_tz.broadcast.ReminderBroadcast
import com.example.senla_tz.databinding.FragmentReminderBinding
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.service.ReminderNotificationService
import com.example.senla_tz.ui.activity.main.IMainNavController
import com.example.senla_tz.ui.dialog.ReminderDateDialog
import com.example.senla_tz.util.resourse.RecyclerViewDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


private val TAG = ReminderFragment::class.simpleName
@AndroidEntryPoint
class ReminderFragment : Fragment(R.layout.fragment_reminder) {

    private var mainNavController: IMainNavController? = null
    private var binding: FragmentReminderBinding? = null
    private var adapterReminder: AdapterReminder? = null

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

            //recReminder.addItemDecoration(RecyclerViewDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.decoration_white)!!))
        }

        vm.getAllReminder()

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
                    adapterReminder = AdapterReminder(it){ reminder ->
                        showDialogReminder(false, reminder.id)
                    }

                    binding?.recReminder?.adapter = adapterReminder!!
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.reminderFlow.collect {
                    addReminder(it.date, it.id)
                    adapterReminder?.addReminder(it)
                }
            }
        }
    }

    private fun showDialogReminder(isFirstStart: Boolean, id: Int? = null){
        val dialog = ReminderDateDialog.newInstance(isFirstStart)
        dialog.callback = object : ReminderDateDialog.Callback{
            override fun createReminder(calendar: Calendar) {
                lifecycleScope.launch {
                    if (id == null) vm.saveReminder(Reminder(id = 0, date = calendar))
                    else {
                        val reminder = Reminder(
                            id = id,
                            date = calendar
                        )
                        adapterReminder?.updateReminder(reminder)
                        vm.updateReminder(reminder)
                    }
                }
            }

            override fun deleteReminder() {
                id?.let {
                    deleteReminder(it)
                    vm.deleteReminderById(it)
                    adapterReminder?.removeReminder(it)
                }
            }

        }

        dialog.show(childFragmentManager,ReminderDateDialog::javaClass.name)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun addReminder(calendar: Calendar, id: Int){
        val intent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            id,
            Intent(requireContext().applicationContext, ReminderBroadcast::class.java).putExtra("id",id),
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
        val intent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            id,
            Intent(requireContext().applicationContext, ReminderBroadcast::class.java),
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
