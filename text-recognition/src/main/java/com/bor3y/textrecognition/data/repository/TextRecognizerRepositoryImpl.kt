package com.bor3y.textrecognition.data.repository

import android.graphics.Bitmap
import com.bor3y.textrecognition.data.recognizer.ImageTextRecognizer
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
import javax.inject.Inject

class TextRecognizerRepositoryImpl @Inject constructor(
    private val imageTextRecognizer: ImageTextRecognizer
) : TextRecognizerRepository {
    override suspend fun getTextFromImage(
        imageBitmap: Bitmap
    ): String {
        return imageTextRecognizer.getTextFromImage(imageBitmap)
    }
}