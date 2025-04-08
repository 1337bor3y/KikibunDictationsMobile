package com.bor3y.dictations_list.data.local

import androidx.paging.PagingData
import com.bor3y.dictations_list.data.local.model.DictationLocal
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun upsertDictations(dictations: List<DictationLocal>)

    fun getDictations(): Flow<PagingData<DictationLocal>>

    suspend fun getDictationsCount(): Int

    suspend fun deleteAllDictations()

    suspend fun deleteOldestDictations(count: Int)

    suspend fun getOldestDictations(count: Int): List<DictationLocal>
}