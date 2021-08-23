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