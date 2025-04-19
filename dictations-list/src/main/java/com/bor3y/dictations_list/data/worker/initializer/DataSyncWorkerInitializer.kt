package com.bor3y.dictations_list.data.worker.initializer

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.worker.DataSyncWorker
import com.bor3y.dictations_list.di.InitializerEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSyncWorkerInitializer : Initializer<WorkManager>, Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun create(context: Context): WorkManager {
        InitializerEntryPoint.resolve(context).inject(this)
        WorkManager.initialize(context, workManagerConfiguration)

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

        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(DependencyGraphInitializer::class.java)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}