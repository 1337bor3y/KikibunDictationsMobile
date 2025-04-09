package com.bor3y.core.data.remote.dto

data class DictationDetailDto(
    val id: String,
    val title: String,
    val text: String,
    val audio_file_dictation: String,
    val audio_file_normal: String,
    val created_at: String,
    val english_level: String
)