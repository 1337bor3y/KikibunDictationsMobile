package com.bor3y.dictations_list.presentation

import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel

sealed interface DictationsListEvent {

    data class SelectDictation(val dictation: Dictation): DictationsListEvent

    data class SelectEnglishLevel(val englishLevel: EnglishLevel): DictationsListEvent
}