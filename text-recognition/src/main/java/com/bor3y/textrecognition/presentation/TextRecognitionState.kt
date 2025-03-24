package com.bor3y.textrecognition.presentation

import android.graphics.Bitmap
import androidx.camera.core.SurfaceRequest
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class TextRecognitionState(
    val surfaceRequest: SurfaceRequest? = null,
    val capturedImage: Bitmap? = null,
    val frameDimensions: FrameDimensions = FrameDimensions(),
    val error: String? = null
)

data class FrameDimensions(
    val size: Size = Size(300f, 200f),
    val position: Offset = Offset(100f, 200f),
)