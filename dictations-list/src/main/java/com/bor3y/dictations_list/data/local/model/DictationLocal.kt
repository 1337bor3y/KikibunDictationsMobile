package com.bor3y.dictations_list.data.local.model

data class DictationLocal(
    val id: String,
    val title: String,
    val text: String,
    val audioFileDictation: String,
    val audioFileNormal: String,
    val createdAt: String,
    val englishLevel: String,
    val isCompleted: Boolean
)
