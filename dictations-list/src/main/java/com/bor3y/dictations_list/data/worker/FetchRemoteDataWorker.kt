package com.bor3y.dictations_list.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toLocal
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FetchRemoteDataWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            
            val remoteListResult = remoteDataSource.getDictations()
            if (remoteListResult.isFailure) {
                logError(
                    "Dictation list error: " + (remoteListResult.exceptionOrNull()?.localizedMessage
                        ?: "Unknown error")
                )
                return@withContext Result.failure()
            }

            remoteListResult.getOrNull()?.let { itemList ->
                if (localDataSource.getDictationsCount() >= Constants.MAX_DICTATIONS_IN_DB) {
                    localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT)
                }

                val detailList = itemList.mapNotNull {
                    val remoteDetailResult = remoteDataSource.getDictationDetail(it.id)
                    remoteDetailResult.exceptionOrNull()?.let { exception ->
                        logError(
                            "Dictation detail error: " + (exception.localizedMessage
                                ?: "Unknown error")
                        )
                        return@withContext Result.failure()
                    }

                    remoteDetailResult.getOrNull()
                }

                localDataSource.upsertDictations(detailList.map { it.toLocal() })

                return@withContext Result.success()
            }

            logError("No data received from the remote server")
            return@withContext Result.failure()
        }
    }

    private fun logError(error: String) {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTime = currentTime.format(formatter)

        // Later log with Firebase Crashlytics
        Log.d(Constants.LogErrors.WORKER_ERROR.name, "$formattedTime: $error")
    }
}