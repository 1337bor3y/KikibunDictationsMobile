package com.bor3y.textrecognition

import android.graphics.Bitmap
import androidx.camera.core.SurfaceRequest
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class TextRecognitionState(
    val surfaceRequest: SurfaceRequest? = null,
    val capturedImage: Bitmap? = null,
    val recognizedText: String? = null,
    val dimensions: Dimensions? = null,
    val error: String? = null
)

data class Dimensions(
    val frameSize: Size,
    val framePosition: Offset,
    val screenSize: Size
)
