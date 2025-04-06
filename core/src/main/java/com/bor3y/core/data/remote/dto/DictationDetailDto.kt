package com.bor3y.core.data.remote.dto

data class DictationDetailDto(
    val id: String,
    val title: String,
    val text: String,
    val audioFileDictation: String,
    val audioFileNormal: String,
    val createdAt: String,
    val englishLevel: String
)