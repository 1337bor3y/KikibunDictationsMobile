package com.bor3y.dictation_detail.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.bor3y.text_accuracy_lib.TextAccuracy

data class DictationDetailState(
    val isNormalSpeed: Boolean = true,
    val audioFileDictation: String? = null,
    val audioFileNormal: String? = null,
    val transcription: String = "",
    val typedText: String = "",
    val duration: Long = 0,
    val currentPosition: Long = 0,
    val isPlaying: Boolean = false,
    val accuracyResult:  Map<Double, List<TextAccuracy. Operation>>? = null,
    val showAccuracyDialog: Boolean = false,
    val showCameraScreen: Boolean = false,
    val selectedImageBitmap: ImageBitmap? = null,
    val previewImage: Boolean = false
)
