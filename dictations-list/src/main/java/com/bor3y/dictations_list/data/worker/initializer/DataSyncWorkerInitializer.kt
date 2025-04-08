package com.bor3y.dictations_list.data.worker.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.worker.DataSyncWorker
import com.google.auto.service.AutoService
import java.util.concurrent.TimeUnit

@AutoService(Initializer::class)
class DataSyncWorkerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val appContext = context.applicationContext
        val workManager = WorkManager.getInstance(appContext)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            Constants.WORKER_REPEAT_INTERVAL, TimeUnit.HOURS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            Constants.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}