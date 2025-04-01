package com.bor3y.textrecognition.domain.use_case

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.bor3y.textrecognition.domain.model.Resource
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetTextFromImageUseCaseTest {

    private lateinit var getTextFromImageUseCase: GetTextFromImageUseCase
    private val recognizerRepository: TextRecognizerRepository = mockk()

    @Before
    fun setUp() {
        // Initialize use case with mocked repository
        getTextFromImageUseCase = GetTextFromImageUseCase(recognizerRepository)
    }

    @Test
    fun `invoke should return text successfully`() = runTest {
        // Given
        val mockBitmap: Bitmap = mockk() // Create a mock bitmap if needed
        val framePosition = Offset(50f, 50f)
        val frameSize = Size(200f, 200f)
        val screenSize = Size(1080f, 1920f)
        val expectedText = "Recognized text"

        // Mock the repository to return a known result
        coEvery { recognizerRepository.getTextFromImage(any()) } returns expectedText

        // When
        val resultFlow = getTextFromImageUseCase.invoke(mockBitmap, framePosition, frameSize, screenSize)

        // Then
        resultFlow.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    assertEquals(expectedText, resource.data)
                    assertNull(resource.errorMessage)
                }
                else -> {
                }
            }
        }
    }

    @Test
    fun `invoke should return error on exception`() = runTest {
        // Given
        val mockBitmap: Bitmap = mockk() // Create a mock bitmap if needed
        val framePosition = Offset(50f, 50f)
        val frameSize = Size(200f, 200f)
        val screenSize = Size(1080f, 1920f)

        // Mock the repository to return a known result
        coEvery { recognizerRepository.getTextFromImage(any()) } throws Exception()

        // When
        val resultFlow = getTextFromImageUseCase.invoke(mockBitmap, framePosition, frameSize, screenSize)

        // Then
        resultFlow.collect { resource ->
            when (resource) {
                is Resource.Error -> {
                    assertNotNull(resource.errorMessage)
                    assertNull(resource.data)
                }
                else -> {
                }
            }
        }
    }
}