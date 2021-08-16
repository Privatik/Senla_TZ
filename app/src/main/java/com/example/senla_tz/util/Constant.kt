package com.example.senla_tz.util

import com.google.gson.annotations.SerializedName

object Constant {
    const val BASE_URL = "https://pub.zame-dev.org/"

    const val ENDPOINT_REGISTER = "senla-training-addition/lesson-26.php?method=register"
    const val ENDPOINT_LOGIN = "senla-training-addition/lesson-26.php?method=login"
    const val ENDPOINT_TRACKS = "senla-training-addition/lesson-26.php?method=tracks"


    const val errorFromService = "Что-то пошло не так.."

    enum class StatusResponse{
        @SerializedName("ok")
        OK,
        @SerializedName("error")
        ERROR,
    }
}