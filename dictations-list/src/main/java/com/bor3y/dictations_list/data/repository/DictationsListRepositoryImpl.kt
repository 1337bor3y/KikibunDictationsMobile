package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toDomain
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DictationsListRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : DictationsListRepository {

    override fun getDictationsByEnglishLevel(englishLevel: String): Flow<PagingData<Dictation>> {

        return localDataSource.getDictationsByEnglishLevel(englishLevel)
            .map { data -> data.map { it.toDomain(it.createdAt.isToday()) } }
    }

    private fun String.isToday(): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return try {
            val createdDate = LocalDateTime.parse(this, formatter).toLocalDate()
            createdDate.isEqual(LocalDate.now())
        } catch (e: Exception) {
            false
        }
    }
}