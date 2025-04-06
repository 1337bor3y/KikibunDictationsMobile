package com.bor3y.dictations_list.data.remote

import com.bor3y.core.data.remote.DictationApi
import com.bor3y.core.data.remote.dto.DictationItemDto
import com.bor3y.dictations_list.data.mapper.toDto
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RetrofitRemoteDataSourceTest {

    private lateinit var api: DictationApi
    private lateinit var dataSource: RetrofitRemoteDataSource

    @Before
    fun setUp() {
        // Create a mock of the DictationApi
        api = mockk(relaxed = true)
        // Initialize RetrofitDataSource with the mocked API
        dataSource = RetrofitRemoteDataSource(api)
    }

    @Test
    fun `getDictations should return a Result containing a list of dictations`() = runTest {
        // Arrange: Create mock dictation items that the API should return
        val mockRemoteDictation = mockk<DictationItemRemote>(relaxed = true)
        val mockApiResponse =  Result.success(listOf(mockRemoteDictation.toDto()))

        // Mock api.getDictations() to return a successful response with mock data
        coEvery { api.getDictations() } returns mockApiResponse

        // Act: Call the method under test
        val result = dataSource.getDictations()

        // Assert: Check that the result is a success and contains the expected list
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)

        // Verify that the API method was called
        coVerify { api.getDictations() }
    }

    @Test
    fun `getDictations should return an error result if the API call fails`() = runTest {
        // Arrange: Simulate an API failure
        val mockApiResponse =  Result.failure<List<DictationItemDto>>(Exception("API failure"))
        coEvery { api.getDictations() } returns mockApiResponse

        // Act: Call the method under test
        val result = dataSource.getDictations()

        // Assert: Check that the result is a failure and contains the exception
        assertTrue(result.isFailure)
        assertEquals("API failure", result.exceptionOrNull()?.message)

        // Verify that the API method was called
        coVerify { api.getDictations() }
    }
}

