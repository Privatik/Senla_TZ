package com.example.senla_tz.entify

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Track", indices  = [Index(value = ["idServer"], unique = true)])
data class Track(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var idServer: Long,
    var beginsAt: Long,
    var time: Long,
    var distance: Long
)
