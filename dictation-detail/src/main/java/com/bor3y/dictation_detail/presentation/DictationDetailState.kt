package com.bor3y.dictation_detail.presentation

data class DictationDetailState(
    val isNormalSpeed: Boolean = true,
    val audioFileDictation: String? = null,
    val audioFileNormal: String? = null,
    val transcription: String = "",
    val duration: Long = 0,
    val currentPosition: Long = 0,
    val isPlaying: Boolean = false
)
