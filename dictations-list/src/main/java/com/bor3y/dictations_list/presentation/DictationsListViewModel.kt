package com.bor3y.dictations_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel
import com.bor3y.dictations_list.domain.use_case.GetDictationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DictationsListViewModel @Inject constructor(
    getDictations: GetDictationsUseCase,
) : ViewModel() {

    private val _englishLevel = MutableStateFlow(EnglishLevel.A1)
    private val _dictationPagingData = _englishLevel
        .flatMapLatest { englishLevel ->
            getDictations().map { pagingData: PagingData<Dictation> ->
                pagingData.filter { it.englishLevel == englishLevel.toString() }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())
    private val _state = MutableStateFlow(DictationsListState())
    val state = combine(_state, _englishLevel) { state, englishLevel ->
        state.copy(
            dictationPagingData = _dictationPagingData,
            englishLevel = englishLevel
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DictationsListState())

    fun onEvent(event: DictationsListEvent) {
        when (event) {
            is DictationsListEvent.SelectDictation -> TODO()
            is DictationsListEvent.SelectEnglishLevel -> TODO()
        }
    }
}