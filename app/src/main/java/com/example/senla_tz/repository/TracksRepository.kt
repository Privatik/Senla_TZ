package com.example.senla_tz.repository

import android.util.Log
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import com.example.senla_tz.repository.network.data.PointResponse
import com.example.senla_tz.repository.network.data.SaveTrackRequest
import com.example.senla_tz.repository.network.data.TrackResponse
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.util.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import javax.inject.Inject

private val TAG = TracksRepository::class.simpleName
class TracksRepository @Inject constructor(
    private var dao: TrackDao,
    private var service: TracksApi,
    private var pref: TokenPref
) {

    val tracksFlow = MutableSharedFlow<List<Track>>()
    val tracksFailFlow = MutableSharedFlow<String>()

    suspend fun getTracks(){
        try {
            val listFromBase = dao.getAllTracks()

            if (listFromBase.isNotEmpty()) tracksFlow.emit(listFromBase)
            val res = service.getAllTracks(pref.getToken())

            if (res.status == Constant.StatusResponse.OK) {
                val listFromService = res.tracks.map {
                    Track(
                        id = 0,
                        idServer = it.id,
                        beginsAt = it.beginsAt,
                        time = it.time,
                        distance = it.distance
                    )
                }
                Log.e(TAG,"close ${res.tracks}")


                if (listFromBase.isEmpty()) dao.saveTracks(listFromService)
                updateDataOnService(listFromBase)

                if(listFromBase.isEmpty()) tracksFlow.emit(listFromService)

            } else {
                tracksFailFlow.emit(res.code!!.text)
            }

        }catch (e: Exception){
            Log.e(TAG,e.message?: e.toString())
            tracksFailFlow.emit(Constant.ERROR_FROM_SERVICE)
        }
    }

    private suspend fun updateDataOnService(list: List<Track>){
        val token = pref.getToken()

        list.filter { !it.isHasService }.forEach { track ->
            try {
                val request = SaveTrackRequest(
                    token = token.token,
                    beginsAt = track.beginsAt,
                    time = track.time,
                    distance = track.distance,
                    points = track.points.map { PointResponse(it.lng, it.lat) }
                )

                val res = service.saveTrack(request)

                if (res.status == Constant.StatusResponse.OK){
                    track.idServer = res.id
                    track.isHasService = true
                    dao.saveTrack(track)
                }else{
                   Log.e(TAG,"errorSave ${res.code}")
                }

            }catch (e: Exception){
                Log.e(TAG,"Save ${track.id} with error $e")
            }
        }
    }
}