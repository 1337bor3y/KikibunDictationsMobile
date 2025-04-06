package com.bor3y.dictations_list.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toLocal
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import javax.inject.Inject

class FetchRemoteDataWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val remoteResult = remoteDataSource.getDictations()

        remoteResult.exceptionOrNull()?.let { exception ->
            val errorMessage = exception.localizedMessage ?: "Unknown error"
            val errorData = workDataOf(Constants.WORK_MANAGER_ERROR_KEY to errorMessage)
            return Result.failure(errorData)
        }

        remoteResult.getOrNull()?.let { list ->
            if (localDataSource.getDictationsCount() >= Constants.MAX_DICTATIONS_IN_DB) {
                localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT)
            }
            localDataSource.upsertDictations(list.map { it.toLocal() })
            return Result.success()
        }

        val errorData =
            workDataOf(Constants.WORK_MANAGER_ERROR_KEY to "No data received from the remote server")
        return Result.failure(errorData)
    }
}