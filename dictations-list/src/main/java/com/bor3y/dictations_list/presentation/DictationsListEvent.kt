package com.bor3y.dictations_list.presentation

import com.bor3y.dictations_list.domain.model.EnglishLevel

sealed interface DictationsListEvent {

    data class SelectEnglishLevel(val englishLevel: EnglishLevel): DictationsListEvent

    data object ToggleHideCompleted: DictationsListEvent
}