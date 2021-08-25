package com.example.senla_tz.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.bind_adapter.isSelect
import com.example.senla_tz.databinding.ItemDayOfWeekBinding
import com.example.senla_tz.util.extends.getDayAndMonth
import com.example.senla_tz.util.extends.getRussianShortDayOFWeek
import java.util.*

class AdapterDayOfWeek(): RecyclerView.Adapter<AdapterDayOfWeek.DayViewHolder>() {

    private val listDay: List<DayOfWeek> = Calendar.getInstance().getCurrentWeekWithCurrentDay()
    var currentDay = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder =
        DayViewHolder(
            ItemDayOfWeekBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.binding.apply {
            val dayOfWeek = listDay[position]
            model = dayOfWeek

            root.setOnClickListener {
                listDay[currentDay].isSelected.set(false)
                currentDay = position
                dayOfWeek.isSelected.set(true)
            }
        }
    }

    override fun getItemCount(): Int = 7

    class DayViewHolder(val binding: ItemDayOfWeekBinding): RecyclerView.ViewHolder(binding.root)

    private fun Calendar.getCurrentWeekWithCurrentDay(): List<DayOfWeek>{
        val listDay: MutableList<DayOfWeek> = mutableListOf()

        listDay.add(DayOfWeek(
            "${getRussianShortDayOFWeek(get(Calendar.DAY_OF_WEEK))}\n${getDayAndMonth()}").also {
                it.isSelected.set(true)
        })
        repeat(6){
            add(Calendar.DAY_OF_WEEK,1)
            listDay.add(DayOfWeek("${getRussianShortDayOFWeek(get(Calendar.DAY_OF_WEEK))}\n${getDayAndMonth()}"))
        }

        return listDay
    }
}

data class DayOfWeek(
    val dayText: String
){
    val isSelected: ObservableBoolean by lazy { ObservableBoolean(false) }
}