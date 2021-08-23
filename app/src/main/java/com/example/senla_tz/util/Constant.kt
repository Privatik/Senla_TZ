package com.example.senla_tz.util

import androidx.core.content.ContextCompat
import com.example.senla_tz.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.gson.annotations.SerializedName

object Constant {
    const val BASE_URL = "https://pub.zame-dev.org/"

    const val ENDPOINT_REGISTER = "senla-training-addition/lesson-26.php?method=register"
    const val ENDPOINT_LOGIN = "senla-training-addition/lesson-26.php?method=login"
    const val ENDPOINT_TRACKS = "senla-training-addition/lesson-26.php?method=tracks"


    const val errorFromService = "Что-то пошло не так.."

    const val BASE_NAME = "TRACK_DATABASE"

    const val ME = "Я"

    const val ZOOM_CAMERA = 17f

    enum class StatusResponse{
        @SerializedName("ok")
        OK,
        @SerializedName("error")
        ERROR,
    }
}