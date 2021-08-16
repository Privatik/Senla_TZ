package com.example.senla_tz.di.module

import android.content.Context
import androidx.room.Room
import com.example.senla_tz.repository.database.TrackDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

private const val NAME = "TRACK_DATABASE"
@Module
@InstallIn(ActivityComponent::class)
object RoomModule {

    @ActivityScoped
    @Provides
    fun provideDataBase(
       @ActivityContext context: Context
    ): TrackDataBase =
        Room.databaseBuilder(
            context,
            TrackDataBase::class.java,
            NAME
        ).allowMainThreadQueries()
            .build()

    @ActivityScoped
    @Provides
    fun provideTrackDao(database: TrackDataBase) = database.trackDao()

    @ActivityScoped
    @Provides
    fun provideReminderDao(database: TrackDataBase) = database.reminderDao()
}