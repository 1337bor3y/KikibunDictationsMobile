package com.bor3y.dictations_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bor3y.dictations_list.domain.model.EnglishLevel
import com.bor3y.dictations_list.domain.use_case.GetDictationsByEnglishLevelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DictationsListViewModel @Inject constructor(
    getDictationsByEnglishLevel: GetDictationsByEnglishLevelUseCase
) : ViewModel() {

    private val _englishLevel = MutableStateFlow(EnglishLevel.A1)
    private val _dictationPagingData = _englishLevel.mapLatest { englishLevel ->
        getDictationsByEnglishLevel(englishLevel)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyFlow())
    private val _state = MutableStateFlow(DictationsListState())
    val state =
        combine(_state, _englishLevel, _dictationPagingData) { state, englishLevel, pagingData ->
            state.copy(
                dictationPagingData = pagingData,
                englishLevel = englishLevel
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DictationsListState())

    fun onEvent(event: DictationsListEvent) {
        when (event) {
            is DictationsListEvent.SelectDictation -> TODO()
            is DictationsListEvent.SelectEnglishLevel -> _englishLevel.value = event.englishLevel
        }
    }
}