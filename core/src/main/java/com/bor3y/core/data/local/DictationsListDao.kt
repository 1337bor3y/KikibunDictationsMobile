package com.bor3y.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bor3y.core.data.local.entity.DictationEntity

@Dao
interface DictationsListDao {

    @Upsert
    suspend fun upsertDictations(dictations: List<DictationEntity>)

    @Query("SELECT * FROM dictations WHERE english_level = :englishLevel")
    fun getDictationsByEnglishLevel(englishLevel: String): PagingSource<Int, DictationEntity>

    @Query("SELECT COUNT(*) FROM dictations")
    suspend fun getDictationsCount(): Int

    @Query("DELETE FROM dictations")
    suspend fun deleteAllDictations()

    @Query("DELETE FROM dictations WHERE id IN (SELECT id FROM dictations ORDER BY created_at ASC LIMIT :limit)")
    suspend fun deleteOldestDictations(limit: Int)

    @Query("SELECT * FROM dictations ORDER BY created_at ASC LIMIT :limit")
    suspend fun getOldestDictations(limit: Int): List<DictationEntity>

    @Query("UPDATE dictations SET is_completed = :isCompleted WHERE id = :id")
    suspend fun updateIsCompleted(id: String, isCompleted: Boolean)

    @Query("SELECT * FROM dictations WHERE id = :id")
    suspend fun getDictationById(id: String): DictationEntity?
}