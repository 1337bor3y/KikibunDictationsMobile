package com.bor3y.dictations_list.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bor3y.dictations_list.data.worker.sync_manager.DataSyncManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dataSyncManager: DataSyncManager
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return if (dataSyncManager.sync(applicationContext)) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}