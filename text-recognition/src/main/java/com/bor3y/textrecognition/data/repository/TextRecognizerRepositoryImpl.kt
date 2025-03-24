package com.bor3y.textrecognition.data.repository

import android.graphics.Bitmap
import com.bor3y.textrecognition.data.recognizer.ImageTextRecognizer
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
import javax.inject.Inject

class TextRecognizerRepositoryImpl @Inject constructor(
    private val imageTextRecognizer: ImageTextRecognizer
) : TextRecognizerRepository {
    override fun getTextFromImage(
        imageBitmap: Bitmap,
        onTextRecognized: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        imageTextRecognizer.getTextFromImage(imageBitmap, onTextRecognized, onFailure)
    }
}