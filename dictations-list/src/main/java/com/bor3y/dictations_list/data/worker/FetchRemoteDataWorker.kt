package com.bor3y.dictations_list.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import javax.inject.Inject

class FetchRemoteDataWorker @Inject constructor(
    private val appContext: Context,
    params: WorkerParameters,
    private val dataSyncManager: DataSyncManager
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return if (dataSyncManager.sync(appContext)) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}