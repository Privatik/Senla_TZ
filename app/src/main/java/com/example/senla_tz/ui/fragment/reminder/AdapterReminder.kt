package com.example.senla_tz.ui.fragment.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.R
import com.example.senla_tz.databinding.ItemReminderBinding
import com.example.senla_tz.entify.Reminder

class AdapterReminder(private val list: MutableList<Reminder>, inline val onCLick: (Reminder) -> Unit):
    RecyclerView.Adapter<AdapterReminder.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder =
        ReminderViewHolder(
            ItemReminderBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.binding.imgBtnEditReminder.setOnClickListener {
            onCLick(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    fun addReminder(reminder: Reminder){
        list.add(reminder)

        notifyItemInserted(list.size - 1)
    }

    fun removeReminder(reminder: Reminder){
        val index = list.indexOf(reminder)
        list.remove(reminder)

        notifyItemRemoved(index)
    }

    class ReminderViewHolder(val binding: ItemReminderBinding): RecyclerView.ViewHolder(binding.root)
}