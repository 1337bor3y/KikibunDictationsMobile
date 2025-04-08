package com.bor3y.dictations_list.domain.repository

import androidx.paging.PagingData
import com.bor3y.dictations_list.domain.model.Dictation
import kotlinx.coroutines.flow.Flow

interface DictationsListRepository {

    fun getDictations(): Flow<PagingData<Dictation>>

    fun enqueueFetchRemoteDataWorker()
}