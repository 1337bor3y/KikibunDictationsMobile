package com.bor3y.dictations_list.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.bor3y.core.data.local.DictationsListDao
import com.bor3y.core.data.local.entity.DictationEntity
import com.bor3y.dictations_list.data.local.model.DictationLocal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RoomLocalDataSourceTest {

    private lateinit var dao: DictationsListDao
    private lateinit var dataSource: RoomLocalDataSource

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        dataSource = RoomLocalDataSource(dao)
    }

    @Test
    fun `upsertDictations should call dao upsertDictations`() = runTest {
        // Arrange: Create a list of mocked DictationItemLocal objects to pass to the upsert method
        val dictations = listOf(mockk<DictationLocal>(relaxed = true))

        // Act: Call the upsertDictations method of the data source
        dataSource.upsertDictations(dictations)

        // Assert: Verify that dao.upsertDictations was called with any argument
        coVerify { dao.upsertDictations(any()) }
    }

    @Test
    fun `test getDictations returns correct data`() = runTest {
        // Arrange: Prepare a sample DictationEntity and mock DAO's getDictations method
        val dictationEntity = DictationEntity(
            id = "64220988-c192-45f5-88a3-f5054b166445",
            title = "New Ways to See",
            text = "text",
            audioFileDictation = "audioFileDictation",
            audioFileNormal = "audioFileNormal",
            createdAt = "2025-03-24 14:30:58",
            englishLevel = "A1",
            isCompleted = false
        )

        val pagingSource = object : PagingSource<Int, DictationEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DictationEntity> {
                return LoadResult.Page(
                    data = listOf(dictationEntity),
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, DictationEntity>): Int? = null
        }

        every { dao.getDictationsByEnglishLevel("A1") } returns pagingSource

        // Act: Fetch dictations from the data source
        val result = dataSource.getDictationsByEnglishLevel("A1").asSnapshot()

        // Assert: Verify that the returned data matches the expected list
        val expected = listOf(
            DictationLocal(
                id = "64220988-c192-45f5-88a3-f5054b166445",
                title = "New Ways to See",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-24 14:30:58",
                englishLevel = "A1",
                isCompleted = false
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `getDictationsCount should return count`() = runTest {
        // Arrange: Mock the DAO's getDictationsCount method to return a count of 5
        coEvery { dao.getDictationsCount() } returns 5

        // Act: Call the getDictationsCount method of the data source
        val count = dataSource.getDictationsCount()

        // Assert: Verify that the count returned by the data source is 5
        assertEquals(5, count)

        // Verify: Ensure that the DAO's getDictationsCount method was called
        coVerify { dao.getDictationsCount() }
    }

    @Test
    fun `deleteAllDictations should call dao deleteAllDictations`() = runTest {
        // Act: Call the deleteAllDictations method of the data source
        dataSource.deleteAllDictations()

        // Assert: Verify that the DAO's deleteAllDictations method was called
        coVerify { dao.deleteAllDictations() }
    }

    @Test
    fun `deleteOldestDictations should call dao deleteOldestDictations with correct limit`() =
        runTest {
            // Arrange: Define the number of dictations to delete
            val count = 3

            // Act: Call the deleteOldestDictations method of the data source
            dataSource.deleteOldestDictations(count)

            // Assert: Verify that the DAO's deleteOldestDictations method was called with the correct limit
            coVerify { dao.deleteOldestDictations(limit = count) }
        }

    @Test
    fun `getOldestDictations should return a list of dictations`() = runTest {
        // Arrange: Create a list of mocked DictationEntity objects to simulate the database response
        val entity1 = mockk<DictationEntity>(relaxed = true)
        val entity2 = mockk<DictationEntity>(relaxed = true)

        // Mock dao.getOldestDictations() to return a flow with the mocked entities
        coEvery { dao.getOldestDictations(2) } returns listOf(entity1, entity2)

        // Act: Call the getOldestDictations method of the data source
        val result = dataSource.getOldestDictations(2)

        // Assert: Verify that the result is a list with the expected size (2 items in this case)
        assertEquals(2, result.size)

        // Verify: Ensure that the DAO's getOldestDictations method was called with the correct argument
        coVerify { dao.getOldestDictations(2) }
    }

    @Test
    fun `getDictationById should return dictation when found`() = runTest {
        // Arrange: Prepare a sample DictationEntity and mock DAO's getDictationById method
        val id = "test-id"
        val dictationEntity = mockk<DictationEntity>(relaxed = true)
        coEvery { dao.getDictationById(id) } returns dictationEntity

        // Act: Call the getDictationById method of the data source
        val result = dataSource.getDictationById(id)

        // Assert: Verify that the result is not null and is of type DictationLocal
        assertNotNull(result)
        coVerify { dao.getDictationById(id) }
    }

    @Test
    fun `getDictationById should return null when not found`() = runTest {
        // Arrange: Mock DAO's getDictationById method to return null
        val id = "non-existing-id"
        coEvery { dao.getDictationById(id) } returns null

        // Act: Call the getDictationById method of the data source
        val result = dataSource.getDictationById(id)

        // Assert: Verify that the result is null
        assertNull(result)
        coVerify { dao.getDictationById(id) }
    }
}