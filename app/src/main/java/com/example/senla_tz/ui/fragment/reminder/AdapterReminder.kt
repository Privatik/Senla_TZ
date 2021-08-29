package com.example.senla_tz.ui.fragment.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.databinding.ItemReminderBinding
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.entify.Track

class AdapterReminder(inline val onCLick: (Reminder) -> Unit):
    ListAdapter<Reminder,AdapterReminder.ReminderViewHolder>(MyDiffUtil()){

    private val listReminders = mutableListOf<Reminder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder =
        ReminderViewHolder(
            ItemReminderBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            model = item

            imgBtnEditReminder.setOnClickListener {
                onCLick(item)
            }
        }
    }

    override fun submitList(list: MutableList<Reminder>?) {
        if (list != null) {
            listReminders.clear()
            listReminders.addAll(list)
        }
        super.submitList(list)
    }

    fun updateReminder(reminder: Reminder){
        val reminder = listReminders.first { reminder.id == it.id }
        val index = listReminders.indexOf(reminder)

        super.submitList(listReminders)
        notifyItemChanged(index)
    }

    fun addReminder(reminder: Reminder){
        listReminders.add(reminder)
        super.submitList(listReminders)
        notifyItemInserted(listReminders.size - 1)
    }

    fun removeReminder(id: Int){
        val reminder = listReminders.first { id == it.id }
        val index = listReminders.indexOf(reminder)
        listReminders.remove(reminder)

        super.submitList(listReminders)
        notifyItemRemoved(index)
    }

    class ReminderViewHolder(val binding: ItemReminderBinding): RecyclerView.ViewHolder(binding.root)
}

class MyDiffUtil: DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(
        oldConcert: Reminder,
        newConcert: Reminder
    ) = oldConcert.id == newConcert.id

    override fun areContentsTheSame(
        oldConcert: Reminder,
        newConcert: Reminder
    ) = oldConcert == newConcert
}