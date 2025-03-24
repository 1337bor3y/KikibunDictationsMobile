package com.bor3y.textrecognition.data.recognizer

import android.graphics.Bitmap

interface ImageTextRecognizer {
    suspend fun getTextFromImage(
        imageBitmap: Bitmap
    ): String
}