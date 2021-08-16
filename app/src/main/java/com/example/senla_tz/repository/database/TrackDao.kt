package com.example.senla_tz.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.senla_tz.entify.Track

@Dao
interface TrackDao {

    @Query("SELECT * FROM Track")
    suspend fun getAllTracks(): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrack(track: Track)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun saveTracks(tracks: List<Track>)
}