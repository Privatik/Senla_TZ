package com.example.senla_tz.repository.network

import com.example.senla_tz.repository.network.data.TracksResponse
import com.example.senla_tz.util.Constant
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TracksApi {

    @FormUrlEncoded
    @POST(Constant.ENDPOINT_TRACKS)
    suspend fun getAllTracks(@Field("token") token: String): TracksResponse
}