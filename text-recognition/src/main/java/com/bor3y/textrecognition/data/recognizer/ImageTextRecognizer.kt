package com.bor3y.textrecognition.data.recognizer

import android.graphics.Bitmap

interface ImageTextRecognizer {
    fun getTextFromImage(
        imageBitmap: Bitmap,
        onTextRecognized: (String) -> Unit,
        onFailure: (Exception) -> Unit
    )
}