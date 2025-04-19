package com.bor3y.dictations_list.presentation

import androidx.paging.PagingData
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class DictationsListState(
    val dictationPagingData: Flow<PagingData<Dictation>> = emptyFlow(),
    val englishLevel: EnglishLevel = EnglishLevel.A1,
    val hideCompleted: Boolean = false
)
