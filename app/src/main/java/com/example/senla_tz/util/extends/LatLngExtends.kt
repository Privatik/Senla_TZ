package com.example.senla_tz.util.extends

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun List<LatLng>.distance(): Long{
    var distance = 0f

    reduce{ firstLatLng,  secondLatLng ->
        val res = FloatArray(1)
        Location.distanceBetween(
            firstLatLng.latitude, firstLatLng.longitude,
            secondLatLng.latitude, secondLatLng.longitude,
            res
        )
        distance += res[0]
        secondLatLng
    }

    return distance.toLong()
}