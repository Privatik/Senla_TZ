package com.example.senla_tz.util.extends

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.changeDate(
    hour: Int,
    minute: Int,
    dayCount: Int){

    //add(Calendar.MONTH, if (month != 0) month - get(Calendar.MONTH) else 1)
    add(Calendar.DAY_OF_MONTH, dayCount)
    add(Calendar.HOUR_OF_DAY, hour - get(Calendar.HOUR_OF_DAY))
    add(Calendar.MINUTE, (minute - get(Calendar.MINUTE)))
    set(Calendar.SECOND, 0)

}

fun Calendar.getRussianNameMouth()=
    when(get(Calendar.MONTH)){
        0 -> "Января"
        1 -> "Февраля"
        2 -> "Марта"
        3 -> "Апреля"
        4 -> "Мая"
        5 -> "Июня"
        6 -> "Июля"
        7 -> "Августа"
        8 -> "Сентября"
        9 -> "Октября"
        10 -> "Ноября"
        11 -> "Декабря"
        else -> "Не определен"
    }

fun Calendar.formatDateWithTime(): String{
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(time)
}

fun Calendar.getDayAndMonth(): String{
    return SimpleDateFormat("dd/MM", Locale.ENGLISH).format(time)
}

fun Calendar.getRussianShortDayOFWeek(dayOfWeek: Int): String =
    when (dayOfWeek){
        Calendar.MONDAY -> "Пн"
        Calendar.TUESDAY -> "Вт"
        Calendar.WEDNESDAY-> "Ср"
        Calendar.THURSDAY -> "Чт"
        Calendar.FRIDAY -> "Пт"
        Calendar.SATURDAY -> "Сб"
        Calendar.SUNDAY -> "Вс"
        else -> ""
    }

