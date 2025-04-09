package com.bor3y.dictations_list.data.local

import androidx.paging.PagingData
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.domain.model.EnglishLevel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun upsertDictations(dictations: List<DictationLocal>)

    fun getDictationsByEnglishLevel(englishLevel: String): Flow<PagingData<DictationLocal>>

    suspend fun getDictationsCount(): Int

    suspend fun deleteAllDictations()

    suspend fun deleteOldestDictations(count: Int)

    suspend fun getOldestDictations(count: Int): List<DictationLocal>
}