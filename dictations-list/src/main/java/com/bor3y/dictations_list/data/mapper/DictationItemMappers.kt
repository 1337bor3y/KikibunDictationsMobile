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
)

fun DictationEntity.toLocal() = DictationLocal(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
)

fun DictationItemDto.toRemote() = DictationItemRemote(
    id = id,
    title = title,
    createdAt = createdAt,
    englishLevel = englishLevel,
)

fun DictationDetailDto.toRemote() = DictationDetailRemote(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
)

fun DictationDetailRemote.toLocal() = DictationLocal(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = englishLevel,
)

fun DictationLocal.toDomain(isNew: Boolean) = Dictation(
    id = id,
    title = title,
    text = text,
    audioFileDictation = audioFileDictation,
    audioFileNormal = audioFileNormal,
    createdAt = createdAt,
    englishLevel = EnglishLevel.valueOf(englishLevel),
    isNew = isNew
)