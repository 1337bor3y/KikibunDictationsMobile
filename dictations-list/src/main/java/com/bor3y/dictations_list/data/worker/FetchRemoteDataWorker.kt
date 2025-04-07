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
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FetchRemoteDataWorker @Inject constructor(
    private val appContext: Context,
    params: WorkerParameters,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {

            val remoteListResult = remoteDataSource.getDictations().getOrElse { listException ->
                logError(
                    Constants.LogErrors.DICTATIONS_LIST_SERVER_ERROR.name,
                    listException.localizedMessage
                        ?: "Unknown error"
                )
                return@withContext Result.failure()
            }

            val detailList = remoteListResult.map { item ->
                val detail = remoteDataSource.getDictationDetail(item.id)
                    .getOrElse { detailException ->
                        logError(
                            Constants.LogErrors.DICTATIONS_DETAIL_SERVER_ERROR.name,
                            detailException.localizedMessage ?: "Unknown error"
                        )
                        return@withContext Result.failure()
                    }.toLocal()

                val localNormalPath = saveAudioFile(
                    context = appContext,
                    audioUrl = detail.audioFileNormal
                ) ?: return@withContext Result.failure()

                val localDictationPath = saveAudioFile(
                    context = appContext,
                    audioUrl = detail.audioFileDictation
                ) ?: return@withContext Result.failure()

                detail.copy(
                    audioFileNormal = localNormalPath,
                    audioFileDictation = localDictationPath
                )
            }

            if (localDataSource.getDictationsCount() >= Constants.MAX_DICTATIONS_IN_DB) {
                localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT)
                if (!deleteOldFilesAndClearFromRoom(Constants.OLDEST_DICTATIONS_DELETE_COUNT)) {
                    return@withContext Result.failure()
                }
            }

            localDataSource.upsertDictations(detailList)

            return@withContext Result.success()
        }
    }

    private fun saveAudioFile(context: Context, audioUrl: String): String? {
        return try {
            val fileName = audioUrl.substringAfterLast("/").substringBefore(".mp3")

            val audioFile = File(context.filesDir, fileName)

            val inputStream: InputStream = URL(audioUrl).openStream()
            val outputStream: OutputStream = FileOutputStream(audioFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            inputStream.close()
            outputStream.close()

            audioFile.absolutePath
        } catch (e: Exception) {
            logError(
                Constants.LogErrors.SAVE_AUDIO_FILE_ERROR.name,
                e.localizedMessage ?: "Unknown error"
            )
            null
        }
    }

    private suspend fun deleteOldFilesAndClearFromRoom(count: Int): Boolean {
        val dictations = localDataSource.getOldestDictations(count)

        dictations.forEach {
            if (!(deleteFile(it.audioFileNormal) && deleteFile(it.audioFileDictation))) return false
        }

        localDataSource.deleteOldestDictations(count)

        return true
    }

    private fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            try {
                file.delete()
            } catch (e: Exception) {
                logError(
                    Constants.LogErrors.DELETE_AUDIO_FILE_ERROR.name,
                    "Exception deleting $filePath: ${e.localizedMessage}"
                )
                false
            }
        } else {
            true
        }
    }


    private fun logError(tag: String, msg: String) {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedTime = currentTime.format(formatter)

        // Later log with Firebase Crashlytics
        Log.d(tag, "$formattedTime: $msg")
    }
}