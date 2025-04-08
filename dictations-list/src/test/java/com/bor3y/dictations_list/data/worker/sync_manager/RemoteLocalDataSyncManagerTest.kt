package com.bor3y.dictations_list.data.worker.sync_manager

import android.content.Context
import com.bor3y.core.file_manager.AudioFileManager
import com.bor3y.core.logger.Logger
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toLocal
import com.bor3y.dictations_list.data.remote.RemoteDataSource
import com.bor3y.dictations_list.data.remote.model.DictationDetailRemote
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RemoteLocalDataSyncManagerTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var audioFileManager: AudioFileManager
    private lateinit var logger: Logger
    private lateinit var syncManager: RemoteLocalDataSyncManager
    private lateinit var context: Context

    @Before
    fun setUp() {
        // Initialize mocks for the components used in the test
        remoteDataSource = mockk()
        localDataSource = mockk()
        audioFileManager = mockk()
        logger = mockk()
        context = mockk()

        // Initialize the sync manager with the mocked dependencies
        syncManager = RemoteLocalDataSyncManager(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            audioFileManager = audioFileManager,
            logger = logger
        )
    }

    @Test
    fun `test sync when remote data fetch succeeds and local data is updated`() = runTest {
        // Prepare mock behavior for fetching dictation data from the remote source
        val dictationItemRemote = DictationItemRemote(
            id = "1",
            title = "title",
            createdAt = "createdAt",
            englishLevel = "englishLevel"
        )
        val dictationItemRemotes = listOf(dictationItemRemote)
        val dictationDetailRemote = DictationDetailRemote(
            id = "1",
            title = "title",
            createdAt = "createdAt",
            englishLevel = "englishLevel",
            text = "text",
            audioFileNormal = "local_path_normal",
            audioFileDictation = "local_path_dictation"
        )

        // Mock the remote data source methods
        coEvery { remoteDataSource.getDictations() } returns Result.success(dictationItemRemotes)
        coEvery { remoteDataSource.getDictationDetail(dictationItemRemote.id) } returns
                Result.success(dictationDetailRemote)

        // Mock file saving behavior
        coEvery {
            audioFileManager.saveFile(context, dictationDetailRemote.audioFileNormal)
        } returns "local_path_normal"
        coEvery {
            audioFileManager.saveFile(context, dictationDetailRemote.audioFileDictation)
        } returns "local_path_dictation"

        // Mock local data source behavior
        coEvery { localDataSource.getDictationsCount() } returns 0
        coEvery { localDataSource.upsertDictations(any()) } just Runs

        // Run the sync operation
        val result = syncManager.sync(context)

        // Assertions to verify the sync worked as expected
        assertTrue(result)

        // Verify that the data was upsert and files were saved
        coVerify { localDataSource.upsertDictations(any()) }
        coVerify { audioFileManager.saveFile(context, dictationDetailRemote.audioFileNormal) }
        coVerify { audioFileManager.saveFile(context, dictationDetailRemote.audioFileDictation) }
    }

    @Test
    fun `test sync when remote data fetch fails`() = runTest {
        // Prepare mock behavior for the remote data source to simulate failure
        coEvery { remoteDataSource.getDictations() } returns Result.failure(Exception("Network error"))

        // Mock the logger behavior so it does not throw an exception
        coEvery { logger.logError(any(), any()) } just Runs

        // Run the sync operation and assert it fails
        val result = syncManager.sync(context)

        // Assertions
        assertFalse(result)

        // Verify that the logger was called with the expected error message
        coVerify {
            logger.logError(Constants.LogErrors.DICTATIONS_LIST_SERVER_ERROR.name, "Network error")
        }

        // Verify that the local data was not updated
        coVerify(exactly = 0) { localDataSource.upsertDictations(any()) }
    }

    @Test
    fun `test sync when local database exceeds max limit and deletes oldest dictations`() =
        runTest {
            // Prepare mock behavior for dictation data and local database behavior
            val dictationItemRemote = DictationItemRemote(
                id = "1",
                title = "title",
                createdAt = "createdAt",
                englishLevel = "englishLevel"
            )
            val dictationItemRemotes = listOf(dictationItemRemote)
            val dictationDetailRemote = DictationDetailRemote(
                id = "1",
                title = "title",
                createdAt = "createdAt",
                englishLevel = "englishLevel",
                text = "text",
                audioFileNormal = "local_path_normal",
                audioFileDictation = "local_path_dictation"
            )

            // Mock the remote data source methods
            coEvery { remoteDataSource.getDictations() } returns Result.success(dictationItemRemotes)
            coEvery { remoteDataSource.getDictationDetail(dictationItemRemote.id) } returns Result.success(
                dictationDetailRemote
            )

            // Mock file saving behavior
            coEvery {
                audioFileManager.saveFile(context, dictationDetailRemote.audioFileNormal)
            } returns "local_path_normal"
            coEvery {
                audioFileManager.saveFile(context, dictationDetailRemote.audioFileDictation)
            } returns "local_path_dictation"

            // Mock local data source behavior
            coEvery { localDataSource.getDictationsCount() } returns Constants.MAX_DICTATIONS_IN_DB
            coEvery { localDataSource.getOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT) } returns
                    listOf(dictationDetailRemote.toLocal())
            coEvery { localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT) } just Runs
            coEvery { audioFileManager.deleteFile(any()) } returns true
            coEvery { localDataSource.upsertDictations(any()) } just Runs

            // Run the sync operation
            val result = syncManager.sync(context)

            // Assertions
            assertTrue(result)

            // Verify that the oldest dictations were deleted
            coVerify { localDataSource.deleteOldestDictations(Constants.OLDEST_DICTATIONS_DELETE_COUNT) }
            coVerify { audioFileManager.deleteFile(any()) }
        }

    @Test
    fun `test sync when file saving fails`() = runTest {
        // Prepare mock behavior for file saving failure
        val dictationItemRemote = DictationItemRemote(
            id = "1",
            title = "title",
            createdAt = "createdAt",
            englishLevel = "englishLevel"
        )
        val dictationItemRemotes = listOf(dictationItemRemote)
        val dictationDetailRemote = DictationDetailRemote(
            id = "1",
            title = "title",
            createdAt = "createdAt",
            englishLevel = "englishLevel",
            text = "text",
            audioFileNormal = "local_path_normal",
            audioFileDictation = "local_path_dictation"
        )

        // Mock the remote data source to return successful data fetch
        coEvery { remoteDataSource.getDictations() } returns Result.success(dictationItemRemotes)
        coEvery { remoteDataSource.getDictationDetail(dictationItemRemote.id) } returns
                Result.success(dictationDetailRemote)

        // Mock file saving to simulate failure on saving the normal audio file
        coEvery {
            audioFileManager.saveFile(context, dictationDetailRemote.audioFileNormal)
        } returns null
        coEvery {
            audioFileManager.saveFile(context, dictationDetailRemote.audioFileDictation)
        } returns "local_path_dictation"

        // Run the sync operation and assert it fails
        val result = syncManager.sync(context)

        // Assertions
        assertFalse(result)

        // Verify that no dictations were upsert due to the failure in saving the file
        coVerify(exactly = 0) { localDataSource.upsertDictations(any()) }
    }
}