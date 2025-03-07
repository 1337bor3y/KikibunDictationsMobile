package com.bor3y.textrecognition

import android.graphics.Bitmap
import androidx.camera.core.SurfaceRequest

data class TextRecognitionState(
    val surfaceRequest: SurfaceRequest? = null,
    val capturedImage: Bitmap? = null
)
