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

    data class UpdateFrameDimensions(val frameSize: Size, val framePosition: Offset) :
        TextRecognitionEvent

    data class AnalyzeImage(val screenSize: Size, val onTextRecognized: (String) -> Unit) :
        TextRecognitionEvent
}