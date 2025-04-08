package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toDomain
import com.bor3y.dictations_list.data.worker.FetchRemoteDataWorker
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DictationsListRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val workManager: WorkManager
) : DictationsListRepository {

    override fun getDictations(): Flow<PagingData<Dictation>> {
        return localDataSource.getDictations().map { data -> data.map { it.toDomain() } }
    }

    override fun enqueueFetchRemoteDataWorker() {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<FetchRemoteDataWorker>(
                Constants.WORKER_REPEAT_INTERVAL,
                TimeUnit.HOURS
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
}