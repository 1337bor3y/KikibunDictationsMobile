package com.bor3y.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bor3y.core.data.local.entity.DictationItemEntity

@Dao
interface DictationsListDao {

    @Upsert
    suspend fun upsertDictations(dictations: List<DictationItemEntity>)

    @Query("SELECT * FROM dictationitementity")
    suspend fun getDictations(): List<DictationItemEntity>

    @Delete
    suspend fun deleteDictations(dictations: List<DictationItemEntity>)
}