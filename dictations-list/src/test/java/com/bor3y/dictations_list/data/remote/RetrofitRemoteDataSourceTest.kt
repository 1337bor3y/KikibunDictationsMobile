package com.bor3y.dictations_list.data.remote

import com.bor3y.core.data.remote.DictationApi
import com.bor3y.core.data.remote.dto.DictationDetailDto
import com.bor3y.core.data.remote.dto.DictationItemDto
import com.bor3y.dictations_list.data.mapper.toRemote
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
        api = mockk(relaxed = true)
        dataSource = RetrofitRemoteDataSource(api)
    }

    @Test
    fun `getDictations should return a Result containing a list of dictations`() = runTest {
        // Arrange: Create mock dictation items that the API should return
        val mockRemoteDictation = mockk<DictationItemDto>(relaxed = true)
        val mockApiResponse = listOf(mockRemoteDictation)

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
        val mockApiResponse = Exception("API failure")
        coEvery { api.getDictations() } throws mockApiResponse

        // Act: Call the method under test
        val result = dataSource.getDictations()

        // Assert: Check that the result is a failure and contains the exception
        assertTrue(result.isFailure)
        assertEquals("API failure", result.exceptionOrNull()?.message)

        // Verify that the API method was called
        coVerify { api.getDictations() }
    }

    @Test
    fun `getDictationDetail should return a Result containing dictation detail`() = runTest {
        // Arrange: Create a mock dictation detail that the API should return
        val mockDictationDetail = mockk<DictationDetailDto>(relaxed = true)

        // Mock api.getDictationDetail() to return a successful response with mock data
        coEvery { api.getDictationDetail(any()) } returns mockDictationDetail

        // Act: Call the method under test
        val result = dataSource.getDictationDetail("123")

        // Assert: Check that the result is a success and contains the expected detail
        assertTrue(result.isSuccess)
        assertEquals(mockDictationDetail.toRemote(), result.getOrNull())

        // Verify that the API method was called
        coVerify { api.getDictationDetail("123") }
    }

    @Test
    fun `getDictationDetail should return an error result if the API call fails`() = runTest {
        // Arrange: Simulate an API failure
        val mockApiResponse = Exception("API failure")
        coEvery { api.getDictationDetail(any()) } throws mockApiResponse

        // Act: Call the method under test
        val result = dataSource.getDictationDetail("123")

        // Assert: Check that the result is a failure and contains the exception
        assertTrue(result.isFailure)
        assertEquals("API failure", result.exceptionOrNull()?.message)

        // Verify that the API method was called
        coVerify { api.getDictationDetail("123") }
    }
}
