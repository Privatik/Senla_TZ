package com.example.senla_tz.repository

import android.location.Location
import android.util.Log
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import com.example.senla_tz.repository.network.data.PointResponse
import com.example.senla_tz.repository.network.data.PointsRequest
import com.example.senla_tz.repository.network.data.SaveTrackRequest
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.util.Constant
import com.example.senla_tz.util.Constant.ERROR_FROM_SERVICE
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import java.net.ConnectException


private val TAG = RunRepository::class.java.simpleName
private const val NO_NETWORK = "Нет сети"
class RunRepository constructor(
    private val dao: TrackDao,
    private val service: TracksApi,
    private val tokenPref: TokenPref
) {

    val locationFlow = MutableSharedFlow<List<LatLng>>(replay = 1)

    val pointCurrentFlow = MutableSharedFlow<List<LatLng>>()
    val trackFailFlow = MutableSharedFlow<String>()

    private val listLatLng = mutableListOf<LatLng>()

    fun emitLocationFlow(location: Location) {
        Log.e(TAG,"new points")
        val latLng = LatLng(location.latitude, location.longitude)

        if (latLng != listLatLng.lastOrNull()) listLatLng.add(latLng)

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

    suspend fun getPoints(id: Long){
        try {

            val request = PointsRequest(
                id = id,
                token = tokenPref.getToken().token
            )

            val res = service.pointsTrack(request)

            if (res.status == Constant.StatusResponse.OK){
                val points = res.points.map {
                    LatLng(it.lat,it.lng)
                }

                pointCurrentFlow.emit(points)
            }else{
                trackFailFlow.emit(res.code!!.text)
            }

        }catch (e: ConnectException){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(NO_NETWORK)
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(ERROR_FROM_SERVICE)
        }
    }

    suspend fun saveTrack(track: Track){
        try {
            val request = SaveTrackRequest(
                token = tokenPref.getToken().token,
                beginsAt = track.beginsAt,
                time = track.time,
                distance = track.distance,
                points = track.points.map { PointResponse(it.lng, it.lat) }
            )

            val res = service.saveTrack(request)

            if (res.status == Constant.StatusResponse.OK){
                track.idServer = res.id
                dao.saveTrack(track)
            } else{
                track.isHasService = false
                dao.saveTrack(track)
                trackFailFlow.emit(res.code!!.text)
            }

        }catch (e: ConnectException){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(NO_NETWORK)
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(ERROR_FROM_SERVICE)
        }
    }

}