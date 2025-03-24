package com.bor3y.textrecognition.data.recognizer

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject

class MLKitTextRecognizer @Inject constructor() : ImageTextRecognizer {
    override fun getTextFromImage(
        imageBitmap: Bitmap,
        onTextRecognized: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val image = InputImage.fromBitmap(
            imageBitmap,
            0
        )

        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                onTextRecognized(visionText.text)
            }
            .addOnFailureListener(onFailure)
    }
}