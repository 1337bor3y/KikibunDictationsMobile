package com.bor3y.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DictationItemEntity(
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "english_level")
    val englishLevel: String,
    @PrimaryKey
    val id: String,
    val title: String
)