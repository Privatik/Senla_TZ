package com.example.senla_tz.ui.fragment.reminder

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.databinding.FragmentReminderBinding
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.ui.activity.main.IMainNavController
import com.example.senla_tz.ui.dialog.reminder.ReminderDateDialog
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

            adapterReminder = AdapterReminder { reminder ->
                showDialogReminder(false, reminder.date, reminder.id)
            }

            recReminder.adapter = adapterReminder!!
            //recReminder.addItemDecoration(RecyclerViewDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.decoration_white)!!))
        }

        initListener()
        initObserver()

    }

    private fun initListener() {
        binding?.apply {
            fabAddReminder.setOnClickListener {
              showDialogReminder(true, Calendar.getInstance())
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.remindersFlow.collect {
                   adapterReminder?.submitList(it)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.reminderFlow.collect {
                    vm.addReminderNotify(it)
                    adapterReminder?.addReminder(it)
                }
            }
        }
    }

    private fun showDialogReminder(isFirstStart: Boolean, calendar: Calendar ,id: Int? = null){
        val dialog = ReminderDateDialog.newInstance(isFirstStart, calendar)
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
                    vm.deleteReminderById(it)
                    adapterReminder?.removeReminder(it)
                }
            }

        }

        dialog.show(childFragmentManager, ReminderDateDialog::javaClass.name)
    }

    override fun onResume() {
        super.onResume()
        vm.getAllReminder()
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
