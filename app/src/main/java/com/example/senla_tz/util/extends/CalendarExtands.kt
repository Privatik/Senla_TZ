package com.example.senla_tz.util.extends

import java.util.*

fun Calendar.changeDate(
    hour: Int,
    minute: Int,
    day: Int,
    month: Int){

    add(Calendar.MONTH, if (month != 0) month - get(Calendar.MONTH) else 1)
    add(Calendar.DAY_OF_MONTH, day - get(Calendar.DAY_OF_MONTH))
    add(Calendar.HOUR_OF_DAY, hour - get(Calendar.HOUR_OF_DAY))
    add(Calendar.MINUTE, (minute - get(Calendar.MINUTE))-1)

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
