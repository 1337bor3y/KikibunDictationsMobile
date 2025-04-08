package com.bor3y.dictations_list.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.bor3y.dictations_list.data.local.LocalDataSource
import com.bor3y.dictations_list.data.mapper.toDomain
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DictationsListRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : DictationsListRepository {

    override fun getDictations(): Flow<PagingData<Dictation>> {
        return localDataSource.getDictations().map { data -> data.map { it.toDomain() } }
    }
}