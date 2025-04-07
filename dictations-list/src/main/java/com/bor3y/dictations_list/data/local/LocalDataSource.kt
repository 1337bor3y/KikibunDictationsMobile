package com.bor3y.dictations_list.data.local

import com.bor3y.dictations_list.data.local.model.DictationLocal
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun upsertDictations(dictations: List<DictationLocal>)

    fun getDictations(): Flow<List<DictationLocal>>

    suspend fun getDictationsCount(): Int

    suspend fun deleteAllDictations()

    suspend fun deleteOldestDictations(count: Int)

    suspend fun getOldestDictations(count: Int): List<DictationLocal>
}