package com.example.senla_tz.repository.database

import androidx.room.*
import com.example.senla_tz.entify.Track

@Dao
interface TrackDao {

    @Query("SELECT * FROM Track")
    suspend fun getAllTracks(): List<Track>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTrack(track: Track)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveTracks(tracks: List<Track>)

    @Query("DELETE FROM Track")
    suspend fun deleteAllTrack()

    @Query("DELETE FROM Point")
    suspend fun deleteAllTPoint()

    @Transaction
    suspend fun deleteAll(){
        deleteAllTrack()
        deleteAllTPoint()
    }
}