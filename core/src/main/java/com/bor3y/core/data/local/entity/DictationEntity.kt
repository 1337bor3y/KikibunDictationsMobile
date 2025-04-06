package com.bor3y.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictations")
class DictationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val text: String,
    @ColumnInfo(name = "audio_file_dictation")
    val audioFileDictation: String,
    @ColumnInfo(name = "audio_file_normal")
    val audioFileNormal: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "english_level")
    val englishLevel: String
)