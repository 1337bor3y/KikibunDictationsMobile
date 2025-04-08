package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.domain.model.Dictation
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DictationsListRepositoryTest {

    private lateinit var repository: DictationsListRepositoryImpl

    private val localDataSource: LocalDataSource = mockk()
    private val workManager: WorkManager = mockk()

    @Before
    fun setUp() {
        repository = DictationsListRepositoryImpl(localDataSource, workManager)
    }

    @Test
    fun `getDictations() should map DictationLocal to Dictation`() = runTest {
        // Arrange: Prepare a list of DictationLocal and mock the localDataSource
        val dictationLocalList = listOf(
            DictationLocal("1", "Title 1", "Text 1", "file1", "file1_normal", "2022-04-01", "B2"),
            DictationLocal("2", "Title 2", "Text 2", "file2", "file2_normal", "2022-04-02", "C1")
        )

        val dictationFlow = flowOf(PagingData.from(dictationLocalList))

        coEvery { localDataSource.getDictations() } returns dictationFlow

        // Act: Fetch dictations from the repository
        val result = repository.getDictations().asSnapshot()

        // Assert: Verify that DictationLocal items are correctly mapped to Dictation
        val expected = listOf(
            Dictation("1", "Title 1", "Text 1", "file1", "file1_normal", "2022-04-01", "B2"),
            Dictation("2", "Title 2", "Text 2", "file2", "file2_normal", "2022-04-02", "C1")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `enqueueFetchRemoteDataWorker() should enqueue periodic work`() {
        // Arrange: Mock workManager.enqueueUniquePeriodicWork
        every {
            workManager.enqueueUniquePeriodicWork(
                any(),
                any(),
                any()
            )
        } returns mockk()

        // Act: Call repository.enqueueFetchRemoteDataWorker
        repository.enqueueFetchRemoteDataWorker()

        // Assert: Verify that workManager.enqueueUniquePeriodicWork was called with correct parameters
        verify {
            workManager.enqueueUniquePeriodicWork(
                Constants.UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                any()
            )
        }
    }
}
