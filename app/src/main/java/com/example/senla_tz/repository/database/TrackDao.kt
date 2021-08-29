package com.example.senla_tz.repository.database

import androidx.room.*
import com.example.senla_tz.entify.Point
import com.example.senla_tz.entify.Track

@Dao
interface TrackDao {

    @Query("SELECT * FROM Track")
    suspend fun getAllTracks(): List<Track>

    @Query("SELECT * FROM point WHERE id = :id")
    suspend fun getPointById(id: Long): List<Point>

    @Query("SELECT * FROM Track JOIN Point ON Track.id = Point.id")
    suspend fun getAllTrackWithPoints(): Map<Track,List<Point>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrack(track: Track): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun saveTracks(tracks: List<Track>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun savePoints(points: List<Point>)

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