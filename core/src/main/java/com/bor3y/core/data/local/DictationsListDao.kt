package com.bor3y.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bor3y.core.data.local.entity.DictationItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DictationsListDao {

    @Upsert
    suspend fun upsertDictations(dictations: List<DictationItemEntity>)

    @Query("SELECT * FROM dictationitementity")
    fun getDictations(): Flow<List<DictationItemEntity>>

    @Query("SELECT COUNT(*) FROM dictationitementity")
    suspend fun getDictationsCount(): Int

    @Query("DELETE FROM dictationitementity")
    suspend fun deleteAllDictations()

    @Query("DELETE FROM dictationitementity WHERE id IN (SELECT id FROM dictationitementity ORDER BY created_at ASC LIMIT :limit)")
    suspend fun deleteOldestDictations(limit: Int)
}