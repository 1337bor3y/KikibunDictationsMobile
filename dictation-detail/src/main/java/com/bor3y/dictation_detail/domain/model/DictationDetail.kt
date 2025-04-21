package com.bor3y.dictation_detail.domain.model

data class DictationDetail(
    val id: String,
    val title: String,
    val text: String,
    val audioFileDictation: String,
    val audioFileNormal: String,
    val englishLevelNumber: String,
    val englishLevelText: String
)