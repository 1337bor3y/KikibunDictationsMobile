package com.bor3y.dictations_list.data.mapper

import com.bor3y.core.data.local.entity.DictationItemEntity
import com.bor3y.core.data.remote.dto.DictationItemDto
import com.bor3y.dictations_list.data.local.model.DictationItemLocal
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote

fun DictationItemLocal.toEntity() = DictationItemEntity(
    createdAt = createdAt,
    englishLevel = englishLevel,
    id = id,
    title = title
)

fun DictationItemEntity.toLocal() = DictationItemLocal(
    createdAt = createdAt,
    englishLevel = englishLevel,
    id = id,
    title = title
)

fun DictationItemDto.toRemote() = DictationItemRemote(
    createdAt = createdAt,
    englishLevel = englishLevel,
    id = id,
    title = title
)

fun DictationItemRemote.toRemote() = DictationItemDto(
    createdAt = createdAt,
    englishLevel = englishLevel,
    id = id,
    title = title
)

fun DictationItemRemote.toLocal() = DictationItemLocal(
    createdAt = createdAt,
    englishLevel = englishLevel,
    id = id,
    title = title
)