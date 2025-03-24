package com.bor3y.textrecognition.domain.repository

import android.graphics.Bitmap

interface TextRecognizerRepository {
    fun getTextFromImage(
        imageBitmap: Bitmap,
        onTextRecognized: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}