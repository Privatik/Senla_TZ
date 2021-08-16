package com.example.senla_tz.repository.network.data

import com.example.senla_tz.util.Constant
import com.google.gson.annotations.SerializedName

data class TracksResponse(
    @SerializedName("status")
    var status: Constant.StatusResponse?,
    @SerializedName("tracks")
    var tracks: List<TrackResponse>
)

data class TrackResponse(
    @SerializedName("id")
    var id: Long,
    @SerializedName("beginsAt")
    var beginsAt: String,
    @SerializedName("time")
    var time: Long,
    @SerializedName("distance")
    var distance: Int
)