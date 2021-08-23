package com.example.senla_tz.service

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.senla_tz.repository.RunRepository
import javax.inject.Inject
import javax.inject.Singleton

class RunWorkerFactory @Inject constructor(
    private val repository: RunRepository
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return TrackLocationWorker(appContext,
            workerParameters,
            repository
        )
    }
}