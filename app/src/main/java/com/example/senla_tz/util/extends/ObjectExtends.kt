package com.example.senla_tz.util.extends

fun Int.normalView(): String =
    if (this > 9){
        "$this"
    } else {
        "0${this}"
    }

fun Long.normalView(): String =
    if (this > 9){
        "$this"
    } else {
        "0${this}"
    }

fun Long.returnTimeFromMilliseconds(): String{
    val minutes = this / 60000
    val seconds = (this - minutes * 60000)/1000
    return "" +
            minutes.normalView() +
            ":${seconds.normalView()}" +
            ":${this.toString().takeLast(3).dropLast(1)}"
}

fun Long.returnTimeWithoutMilliSecondsFromMilliseconds(): String{
    val minutes = this / 60000
    val seconds = (this - minutes * 60000)/1000
    return "" +
            minutes.normalView() +
            ":${seconds.normalView()}"
}