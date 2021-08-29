package com.example.senla_tz.controller

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private val TAG = RunController::class.java.simpleName
class RunController {

    val timerFlow = MutableSharedFlow<Long>(replay = 1)
    val locationFlow = MutableSharedFlow<List<LatLng>>(replay = 1)

    private val listLatLng = mutableListOf<LatLng>()

    private lateinit var foreground:(Boolean) -> Unit
    fun bindForeground(workForeground:(Boolean) -> Unit){
        foreground = workForeground
    }

    fun startForeground(isStart: Boolean){
        if (::foreground.isInitialized) {
            foreground(isStart)
        }
    }

    fun emitLocationFlow(location: Location) {
        Log.e(TAG,"new points")
        val latLng = LatLng(location.latitude, location.longitude)

        if (latLng != listLatLng.lastOrNull()) listLatLng.add(latLng)

        CoroutineScope(Dispatchers.Default).launch {
            locationFlow.emit(listLatLng)
        }
    }

    fun emitTimer(milliseconds: Long){
        CoroutineScope(Dispatchers.Default).launch {
            timerFlow.emit(milliseconds)
        }
    }

    fun updateState(latLng: LatLng?){
        Log.e(TAG,"update isHasLatLmg - $latLng")
        listLatLng.clear()
        if (latLng != null) {
            listLatLng.add(latLng)
        }
    }

}