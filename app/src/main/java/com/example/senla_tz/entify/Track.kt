package com.example.senla_tz.entify

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.senla_tz.util.extends.formatDateWithTime
import com.example.senla_tz.util.extends.returnTimeFromMilliseconds
import com.example.senla_tz.util.extends.returnTimeWithoutMilliSecondsFromMilliseconds
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Track", indices  = [Index(value = ["idServer"], unique = true)])
data class Track(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var idServer: Long,
    var beginsAt: Long,
    var time: Long,
    var distance: Long,
    var isHasService: Boolean = true
): Parcelable{
    @Ignore
    var points: List<Point> = mutableListOf()

    fun parseInDate():String{
        val calendar = Calendar.getInstance().apply {
            timeInMillis = beginsAt
        }

        return calendar.formatDateWithTime()
    }

    fun parseInTime(isWithSeconds: Boolean = false):String{
        if (isWithSeconds) return time.returnTimeFromMilliseconds()
        return time.returnTimeWithoutMilliSecondsFromMilliseconds()
    }

    fun parseInDistance():String{
        return "$distance м"
    }
}

@Entity(tableName = "Point")
data class Point(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var idTrack: Long = 0L,
    var lng: Double,
    var lat: Double
)
