package com.bor3y.textrecognition.data.recognizer

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MLKitTextRecognizer @Inject constructor() : ImageTextRecognizer {
    override suspend fun getTextFromImage(
        imageBitmap: Bitmap
    ): String {
        val image = InputImage.fromBitmap(
            imageBitmap,
            0
        )

        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

        return recognizer.process(image).await().text
    }

    private suspend fun <T> Task<T>.await(): T {
        return suspendCoroutine { continuation ->
            addOnSuccessListener { result -> continuation.resume(result) }
            addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }
    }
}