package com.example.senla_tz.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.senla_tz.repository.RunRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

private val TAG = TrackLocationWorker::class.java.simpleName
@HiltWorker
class TrackLocationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: RunRepository
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            Log.e(TAG,"onWork")
            repository.initLocal()
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failure in doing work $e")
            Result.success()
        }

    }
}