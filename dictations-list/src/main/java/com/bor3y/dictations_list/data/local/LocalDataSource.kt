package com.bor3y.dictations_list.data.local

import com.bor3y.dictations_list.data.local.model.DictationItemLocal
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun upsertDictations(dictations: List<DictationItemLocal>)

    fun getDictations(): Flow<List<DictationItemLocal>>

    suspend fun deleteAllDictations()
}