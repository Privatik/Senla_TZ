package com.example.senla_tz.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

private val TAG = RunRepository::class.java.simpleName
class RunRepository @Inject constructor(
    private val dao: TrackDao,
    private val service: TracksApi,
    @ApplicationContext private val context: Context
) {

    val locationFlow = MutableSharedFlow<Location>()

    @SuppressLint("MissingPermission")
    fun initLocal(){
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.e(TAG,"$location")
                location?.let {
                    locationFlow.tryEmit(it)
                }
            }
    }
}