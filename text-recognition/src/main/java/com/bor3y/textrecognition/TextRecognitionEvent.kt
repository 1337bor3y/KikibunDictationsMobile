package com.bor3y.textrecognition

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.LifecycleOwner

sealed interface TextRecognitionEvent {
    data class BindToCamera(val appContext: Context, val lifecycleOwner: LifecycleOwner) :
        TextRecognitionEvent

    data object TakePhoto : TextRecognitionEvent

    data object CloseImagePreview : TextRecognitionEvent

    data class UpdateDimensions(
        val frameSize: Size,
        val framePosition: Offset,
        val screenSize: Size
    ) : TextRecognitionEvent

    data object AnalyzeImage: TextRecognitionEvent
}