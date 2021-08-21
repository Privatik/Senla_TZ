package com.example.senla_tz.di.module

import android.content.Context
import androidx.room.Room
import com.example.senla_tz.repository.database.ReminderDao
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.database.TrackDataBase
import com.example.senla_tz.util.Constant
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDataBase(
       @ApplicationContext context: Context
    ): TrackDataBase =
        Room.databaseBuilder(
            context,
            TrackDataBase::class.java,
            Constant.BASE_NAME
        ).allowMainThreadQueries()
            .build()

    @Provides
    fun provideTrackDao(database: TrackDataBase): TrackDao = database.trackDao()

    @Provides
    fun provideReminderDao(database: TrackDataBase): ReminderDao = database.reminderDao()
}