package com.bor3y.dictations_list.data.worker.sync_manager

import android.content.Context
import com.bor3y.core.file_manager.AudioFileManager
import com.bor3y.core.logger.Logger
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toLocal
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import javax.inject.Inject

class RemoteLocalDataSyncManager @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val audioFileManager: AudioFileManager,
    private val logger: Logger
) : DataSyncManager {
    override suspend fun sync(context: Context): Boolean {
        val remoteListResult = remoteDataSource.getDictations().getOrElse { listException ->
            logger.logError(
                Constants.LogErrors.DICTATIONS_LIST_SERVER_ERROR.name,
                listException.localizedMessage
                    ?: "Unknown error"
            )
            return false
        }.let { list ->
            if (localDataSource.isDbEmpty()) {
                list.take(36)
            } else list
        }

        val detailList = remoteListResult.map { item ->
            val detail = remoteDataSource.getDictationDetail(item.id)
                .getOrElse { detailException ->
                    logger.logError(
                        Constants.LogErrors.DICTATIONS_DETAIL_SERVER_ERROR.name,
                        detailException.localizedMessage ?: "Unknown error"
                    )
                    return false
                }.toLocal(localDataSource.getDictationById(item.id)?.isCompleted)

            val localNormalPath = audioFileManager.saveFile(
                context = context,
                audioUrl = detail.audioFileNormal
            ) ?: return false

            val localDictationPath = audioFileManager.saveFile(
                context = context,
                audioUrl = detail.audioFileDictation
            ) ?: return false

            detail.copy(
                audioFileNormal = localNormalPath,
                audioFileDictation = localDictationPath
            )
        }

        if (localDataSource.getDictationsCount() >= Constants.MAX_DICTATIONS_IN_DB) {
            localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT)
            if (!deleteOldFilesAndClearFromRoom(Constants.OLDEST_DICTATIONS_DELETE_COUNT)) {
                return false
            }
        }

        localDataSource.upsertDictations(detailList)

        return true
    }

    private suspend fun deleteOldFilesAndClearFromRoom(count: Int): Boolean {
        val dictations = localDataSource.getOldestDictations(count)

        dictations.forEach {
            if (!(audioFileManager.deleteFile(it.audioFileNormal)
                        && audioFileManager.deleteFile(it.audioFileDictation))
            ) return false
        }

        localDataSource.deleteOldestDictations(count)

        return true
    }
}