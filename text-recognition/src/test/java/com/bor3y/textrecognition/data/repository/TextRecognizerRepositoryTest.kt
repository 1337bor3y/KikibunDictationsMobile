package com.bor3y.textrecognition.data.repository

import android.graphics.Bitmap
import com.bor3y.textrecognition.data.recognizer.ImageTextRecognizer
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TextRecognizerRepositoryTest {

    private lateinit var textRecognizerRepository: TextRecognizerRepositoryImpl
    private val imageTextRecognizer: ImageTextRecognizer = mockk()

    @Before
    fun setUp() {
        // Set up the repository with the mocked ImageTextRecognizer
        textRecognizerRepository = TextRecognizerRepositoryImpl(imageTextRecognizer)
    }

    @Test
    fun `getTextFromImage should return expected text`() = runTest {
        // Given
        val mockBitmap: Bitmap = mockk() // You can create a mock Bitmap here if necessary
        val expectedText = "Recognized text"

        // Mock the behavior of the imageTextRecognizer
        coEvery { imageTextRecognizer.getTextFromImage(mockBitmap) } returns expectedText

        // When
        val result = textRecognizerRepository.getTextFromImage(mockBitmap)

        // Then
        assertEquals(expectedText, result)
    }
}