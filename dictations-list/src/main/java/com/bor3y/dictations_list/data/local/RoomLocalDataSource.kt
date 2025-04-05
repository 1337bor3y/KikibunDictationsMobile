package com.bor3y.dictations_list.data.local

import com.bor3y.core.data.local.DictationsListDao
import com.bor3y.dictations_list.data.local.model.DictationItemLocal
import com.bor3y.dictations_list.data.mapper.toEntity
import com.bor3y.dictations_list.data.mapper.toLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(
    private val dao: DictationsListDao
) : LocalDataSource {

    override suspend fun upsertDictations(dictations: List<DictationItemLocal>) {
        dao.upsertDictations(dictations.map { it.toEntity() })
    }

    override fun getDictations(): Flow<List<DictationItemLocal>> {
        return dao.getDictations().map { list -> list.map { it.toLocal() } }
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
}