package com.example.senla_tz.repository.database

import androidx.room.TypeConverter
import java.util.*

class CalendarTypeConverter {

    @TypeConverter
    fun toCalendar(millisecond: Long): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = millisecond
        }

    @TypeConverter
    fun toLong(calendar: Calendar): Long = calendar.timeInMillis
}