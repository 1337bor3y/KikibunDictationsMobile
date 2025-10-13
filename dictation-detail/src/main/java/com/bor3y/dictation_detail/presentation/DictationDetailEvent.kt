package com.bor3y.dictation_detail.presentation

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap

sealed interface DictationDetailEvent {

    data class ChangePlaybackSpeed(val context: Context) : DictationDetailEvent

    data class SetTranscription(val transcription: String) : DictationDetailEvent

    data class SetFilePaths(
        val context: Context,
        val audioFileDictation: String,
        val audioFileNormal: String
    ) :
        DictationDetailEvent

    data object TogglePlayPause : DictationDetailEvent

    data class SeekTo(val position: Long) : DictationDetailEvent

    data class SetTypedText(val text: String) : DictationDetailEvent

    data object FindAccuracy : DictationDetailEvent

    data object HideAccuracyDialog : DictationDetailEvent

    data object OpenCamera : DictationDetailEvent

    data object CloseCamera : DictationDetailEvent

    data class OnGalleryImageSelected(val bitmap: ImageBitmap?) : DictationDetailEvent

    data class OnCameraTextRecognized(val text: String) : DictationDetailEvent
}