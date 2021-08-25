package com.example.senla_tz.repository.network

import com.example.senla_tz.entify.Token
import com.example.senla_tz.repository.network.data.*
import com.example.senla_tz.util.Constant
import retrofit2.http.*

interface TracksApi {

    @POST(Constant.ENDPOINT_TRACKS)
    suspend fun getAllTracks(@Body token: Token): TracksResponse

    @POST(Constant.ENDPOINT_SAVE_TRACK)
    suspend fun saveTrack(@Body request: SaveTrackRequest): SaveTrackResponse

    @POST(Constant.ENDPOINT_POINTS_TRACK)
    suspend fun pointsTrack(@Body request: PointsRequest): PointsResponse
}