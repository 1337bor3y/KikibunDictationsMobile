package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.domain.model.Dictation
import io.mockk.coEvery
import io.mockk.mockk
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

    @Before
    fun setUp() {
        repository = DictationsListRepositoryImpl(localDataSource)
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
}
