package com.bor3y.textrecognition.domain.repository

import android.graphics.Bitmap

interface TextRecognizerRepository {
    suspend fun getTextFromImage(
        imageBitmap: Bitmap
    ): String
}