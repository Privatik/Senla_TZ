package com.example.senla_tz.repository

import android.util.Log
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.network.TracksApi
import com.example.senla_tz.repository.pref.TokenPref
import com.example.senla_tz.util.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import javax.inject.Inject

private val TAG = MainFragmentRepository::class.simpleName
class MainFragmentRepository @Inject constructor(
    private var service: TracksApi,
    private var pref: TokenPref
) {

    private val tracksFlow = MutableSharedFlow<List<Track>>()
    private val tracksFailFlow = MutableSharedFlow<String>()

    suspend fun getTracks(){
        try {
            val res = service.getAllTracks(pref.getToken().text)

            if (res.status == Constant.StatusResponse.OK) {

            } else {

            }

        }catch (e: Exception){
            Log.e(TAG,e.message?: e.toString())
            tracksFailFlow.emit(Constant.errorFromService)
        }
    }
}