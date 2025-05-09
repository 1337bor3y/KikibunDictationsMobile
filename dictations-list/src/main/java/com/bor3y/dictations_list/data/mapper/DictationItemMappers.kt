package com.bor3y.dictations_list.data.mapper

import com.bor3y.core.data.local.entity.DictationEntity
import com.bor3y.core.data.remote.dto.DictationDetailDto
import com.bor3y.core.data.remote.dto.DictationItemDto
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.data.remote.model.DictationDetailRemote
import com.bor3y.dictations_list.data.remote.model.DictationItemRemote
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel

fun DictationLocal.toEntity() = DictationEntity(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
    isCompleted = isCompleted
)

fun DictationEntity.toLocal() = DictationLocal(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
    isCompleted = isCompleted
)

fun DictationItemDto.toRemote() = DictationItemRemote(
    id = id,
    title = title,
    createdAt = created_at,
    englishLevel = english_level,
)

fun DictationDetailDto.toRemote() = DictationDetailRemote(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audio_file_dictation,
    audioFileNormal = audio_file_normal,
    createdAt = created_at,
    englishLevel = english_level,
)

fun DictationDetailRemote.toLocal(isCompleted: Boolean?) = DictationLocal(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
    isCompleted = isCompleted ?: false
)

fun DictationLocal.toDomain(isNew: Boolean) = Dictation(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = EnglishLevel.valueOf(englishLevel),
    isNew = isNew,
    isCompleted = isCompleted
)