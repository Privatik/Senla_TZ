package com.example.senla_tz.di.module

import android.content.Context
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{

    @Singleton
    @Provides
    fun runRepository(
        dao: TrackDao,
        service: TracksApi,
    ) = RunRepository(dao, service)
}