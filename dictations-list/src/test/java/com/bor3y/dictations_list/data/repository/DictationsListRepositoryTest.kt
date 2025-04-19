package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class DictationsListRepositoryTest {

    private lateinit var repository: DictationsListRepositoryImpl

    private val localDataSource: LocalDataSource = mockk()

    @Before
    fun setUp() {
        repository = DictationsListRepositoryImpl(localDataSource)
    }

    @Test
    fun `getDictations() should map DictationLocal to Dictation`() = runTest {
        // Arrange
        // Prepare today's and yesterday's dates formatted as "yyyy-MM-dd HH:mm:ss"
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val yesterday = LocalDateTime.now().minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // Create a list of DictationLocal objects with different createdAt dates
        val dictationLocalList = listOf(
            DictationLocal(
                id = "1",
                title = "Title 1",
                text = "Text 1",
                audioFileDictation = "file1",
                audioFileNormal = "file1_normal",
                createdAt = today,
                englishLevel = "A1",
                isCompleted = false
            ), // created today
            DictationLocal(
                id = "2",
                title = "Title 2",
                text = "Text 2",
                audioFileDictation = "file2",
                audioFileNormal = "file2_normal",
                createdAt = yesterday,
                englishLevel = "A1",
                isCompleted = false
            ) // created yesterday
        )

        // Mock localDataSource to return a flow of PagingData from the list above
        val dictationFlow = flowOf(PagingData.from(dictationLocalList))
        coEvery { localDataSource.getDictationsByEnglishLevel("A1") } returns dictationFlow

        // Act

        // Fetch dictations from repository
        val result = repository.getDictationsByEnglishLevel("A1").asSnapshot()

        // Assert

        // Expected list after mapping
        val expected = listOf(
            Dictation(
                id = "1",
                title = "Title 1",
                text = "Text 1",
                audioFileDictation = "file1",
                audioFileNormal = "file1_normal",
                createdAt = today,
                englishLevel = EnglishLevel.A1,   // Mapping String "A1" to enum EnglishLevel.A1
                isNew = true,
                isCompleted = false
            ),
            Dictation(
                id = "2",
                title = "Title 2",
                text = "Text 2",
                audioFileDictation = "file2",
                audioFileNormal = "file2_normal",
                createdAt = yesterday,
                englishLevel = EnglishLevel.A1,   // Mapping String "A1" to enum EnglishLevel.A1
                isNew = false,
                isCompleted = false
            )
        )

        // Verify that the mapped result matches the expected list
        assertEquals(expected, result)
    }
}
