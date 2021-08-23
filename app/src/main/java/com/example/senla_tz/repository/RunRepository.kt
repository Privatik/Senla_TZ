package com.example.senla_tz.repository

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext


private val TAG = RunRepository::class.java.simpleName
class RunRepository constructor(
    private val dao: TrackDao,
    private val service: TracksApi
) {

    val locationFlow = MutableSharedFlow<List<LatLng>>(replay = 1)

    val trackFailFlow = MutableSharedFlow<String>()

    private val listLatLng = mutableListOf<LatLng>()

    fun emitLocationFlow(list: List<Location>) {
        Log.e(TAG,"new points")
        listLatLng.addAll(list.map { LatLng(it.latitude, it.longitude) })

        CoroutineScope(Dispatchers.Default).launch {
            locationFlow.emit(listLatLng)
        }
    }

    fun updateState(latLng: LatLng?){
        listLatLng.clear()
        if (latLng != null) {
            listLatLng.add(latLng)
        }
    }

    suspend fun saveTrack(track: Track){
        dao.saveTrack(track)

        service
    }

}