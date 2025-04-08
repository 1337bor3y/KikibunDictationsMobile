package com.bor3y.dictations_list.domain.model

data class Dictation(
    val id: String,
    val title: String,
    val text: String,
    val audioFileDictation: String,
    val audioFileNormal: String,
    val createdAt: String,
    val englishLevel: String
)
