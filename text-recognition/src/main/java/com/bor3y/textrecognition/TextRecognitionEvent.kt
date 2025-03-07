package com.bor3y.textrecognition

import android.content.Context
import androidx.lifecycle.LifecycleOwner

sealed interface TextRecognitionEvent {
    data class BindToCamera(val appContext: Context, val lifecycleOwner: LifecycleOwner) :
        TextRecognitionEvent

    data object TakePhoto : TextRecognitionEvent

    data object CloseImagePreview : TextRecognitionEvent
}