package com.bor3y.dictation_detail.presentation

import android.content.Context

sealed interface DictationDetailEvent {

    data class ChangePlaybackSpeed(val context: Context) : DictationDetailEvent

    data class SetTranscription(val transcription: String) : DictationDetailEvent

    data class SetFilePaths(val context: Context, val audioFileDictation: String, val audioFileNormal: String) :
        DictationDetailEvent

    data object TogglePlayPause : DictationDetailEvent

    data class SeekTo(val position: Long) : DictationDetailEvent
}