package com.bor3y.textrecognition

import android.graphics.Bitmap
import androidx.camera.core.SurfaceRequest
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class TextRecognitionState(
    val surfaceRequest: SurfaceRequest? = null,
    val capturedImage: Bitmap? = null,
    val recognizedText: String = "",
    val frameSize: Size? = null,
    val framePosition: Offset? = null,
    val screenSize: Size? = null
)
