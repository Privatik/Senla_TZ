package com.example.senla_tz.entify

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.senla_tz.repository.database.CalendarTypeConverter
import com.example.senla_tz.util.extends.getRussianNameMouth
import com.example.senla_tz.util.extends.normalView
import java.util.*

@Entity
@TypeConverters(CalendarTypeConverter::class)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Calendar
){
    fun getDateToString(): String =
        "${date.get(Calendar.DAY_OF_MONTH).normalView()} ${date.getRussianNameMouth()}" +
                " ${date.get(Calendar.HOUR_OF_DAY).normalView()}" +
                ":${date.get(Calendar.MINUTE).normalView()}"
}
