package com.example.senla_tz.ui.fragment.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.R
import com.example.senla_tz.databinding.ItemReminderBinding
import com.example.senla_tz.entify.Reminder
import com.example.senla_tz.util.extends.addOneMinute

class AdapterReminder(private val list: MutableList<Reminder>, inline val onCLick: (Reminder) -> Unit):
    RecyclerView.Adapter<AdapterReminder.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder =
        ReminderViewHolder(
            ItemReminderBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.binding.apply {
            model = list[position].let {
                it.date.addOneMinute()
                it
            }

            imgBtnEditReminder.setOnClickListener {
                onCLick(list[position])
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateReminder(reminder: Reminder){
        val reminderFromList = list.filter { reminder.id == it.id }.first()
        val index = list.indexOf(reminderFromList)
        list[index] = reminder

        notifyItemChanged(index)
    }

    fun addReminder(reminder: Reminder){
        list.add(reminder)

        notifyItemInserted(list.size - 1)
    }

    fun removeReminder(id: Int){
        val reminder = list.filter { id == it.id }.first()
        val index = list.indexOf(reminder)
        list.remove(reminder)

        notifyItemRemoved(index)
    }

    class ReminderViewHolder(val binding: ItemReminderBinding): RecyclerView.ViewHolder(binding.root)
}