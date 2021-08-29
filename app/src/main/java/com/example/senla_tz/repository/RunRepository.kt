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
import com.example.senla_tz.util.Constant.ERROR_FROM_SERVER
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import java.net.ConnectException
import javax.inject.Inject


private val TAG = RunRepository::class.java.simpleName
private const val NO_NETWORK = "Нет сети"
class RunRepository @Inject constructor(
    private val dao: TrackDao,
    private val service: TracksApi,
    private val tokenPref: TokenPref
) {

    val pointCurrentFlow = MutableSharedFlow<List<LatLng>>()
    val trackFailFlow = MutableSharedFlow<String>()


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
            trackFailFlow.emit(ERROR_FROM_SERVER)
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
            } else{
                track.isHasService = false
                dao.saveTrack(track)
                trackFailFlow.emit(res.code!!.text)
            }

            val id = dao.saveTrack(track)
            track.points.forEach {
                it.idTrack = id
            }

            dao.savePoints(track.points)


        }catch (e: ConnectException){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(NO_NETWORK)
        }catch (e: Exception){
            Log.e(TAG, e.message.toString())
            trackFailFlow.emit(ERROR_FROM_SERVER)
        }
    }

}