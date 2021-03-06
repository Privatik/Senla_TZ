package com.example.senla_tz.di.module

import android.content.Context
import com.example.senla_tz.controller.RunController
import com.example.senla_tz.repository.RunRepository
import com.example.senla_tz.repository.database.TrackDao
import com.example.senla_tz.repository.network.TracksApi
import com.example.senla_tz.repository.pref.TokenPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ControllerModule{

    @Singleton
    @Provides
    fun provideRunController() = RunController()
}