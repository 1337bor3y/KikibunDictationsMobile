package com.bor3y.dictations_list.data.local

import com.bor3y.core.data.local.DictationsListDao
import com.bor3y.core.data.local.entity.DictationEntity
import com.bor3y.dictations_list.data.local.model.DictationLocal
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RoomLocalDataSourceTest {

    private lateinit var dao: DictationsListDao
    private lateinit var dataSource: RoomLocalDataSource

    @Before
    fun setUp() {
        // Initialize the mocked DAO and data source before each test
        dao = mockk(relaxed = true) // relaxed mocks to avoid setting up every return manually
        dataSource =
            RoomLocalDataSource(dao) // Instantiate the RoomLocalDataSource with the mock DAO
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
    fun `getDictations should return flow of dictations`() = runTest {
        // Arrange: Create a mock instance of DictationItemEntity
        val entity = mockk<DictationEntity>(relaxed = true)

        // Mock dao.getDictations() to return a flow containing a list with the mocked entity
        every { dao.getDictations() } returns flowOf(listOf(entity))

        // Act: Call the getDictations method of the data source
        val result = dataSource.getDictations()

        // Assert: Collect the flow and verify that it returns the expected list size
        result.collect { list ->
            assertEquals(1, list.size)
        }

        // Verify: Ensure that the DAO's getDictations method was called
        verify { dao.getDictations() }
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
}