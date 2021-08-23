package com.example.senla_tz.repository.network.data

import com.example.senla_tz.util.Constant
import com.example.senla_tz.util.resourse.FailAuthStatus
import com.example.senla_tz.util.resourse.FailUserStatus
import com.google.gson.annotations.SerializedName

data class TracksResponse(
    @SerializedName("status")
    var status: Constant.StatusResponse?,
    @SerializedName("tracks")
    var tracks: List<TrackResponse>,
    @SerializedName("code")
    var code: FailUserStatus?
)

data class TrackResponse(
    @SerializedName("id")
    var id: Long?,
    @SerializedName("beginsAt")
    var beginsAt: Long,
    @SerializedName("time")
    var time: Long,
    @SerializedName("distance")
    var distance: Int,
    @SerializedName("points")
    var points: List<Point>
)

data class Point(
    @SerializedName("lng")
    var lng: Double,
    @SerializedName("distance")
    var lat: Double
)