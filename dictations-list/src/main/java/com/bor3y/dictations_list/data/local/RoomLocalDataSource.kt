package com.bor3y.dictations_list.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bor3y.core.data.local.DictationsListDao
import com.bor3y.core.until.Constants
import com.bor3y.dictations_list.data.local.model.DictationLocal
import com.bor3y.dictations_list.data.mapper.toEntity
import com.bor3y.dictations_list.data.mapper.toLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(
    private val dao: DictationsListDao
) : LocalDataSource {

    override suspend fun upsertDictations(dictations: List<DictationLocal>) {
        dao.upsertDictations(dictations.map { it.toEntity() })
    }

    override fun getDictationsByEnglishLevel(englishLevel: String): Flow<PagingData<DictationLocal>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.DICTATIONS_LIST_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = Constants.DICTATIONS_LIST_INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { dao.getDictationsByEnglishLevel(englishLevel) }
        ).flow
            .map { pagingData ->
                pagingData.map { dictationEntity ->
                    dictationEntity.toLocal()
                }
            }
    }

    override suspend fun getDictationsCount(): Int {
        return dao.getDictationsCount()
    }

    override suspend fun deleteAllDictations() {
        dao.deleteAllDictations()
    }

    override suspend fun deleteOldestDictations(count: Int) {
        dao.deleteOldestDictations(limit = count)
    }

    override suspend fun getOldestDictations(count: Int): List<DictationLocal> {
        return dao.getOldestDictations(limit = count).map { it.toLocal() }
    }

    override suspend fun getDictationById(id: String): DictationLocal? {
        return dao.getDictationById(id)?.toLocal()
    }
}