package com.bor3y.dictations_list.domain.use_case

import androidx.paging.PagingData
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel
import com.bor3y.dictations_list.domain.repository.DictationsListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDictationsByEnglishLevelUseCase @Inject constructor(
    private val dictationsListRepository: DictationsListRepository
) {
    operator fun invoke(englishLevel: EnglishLevel): Flow<PagingData<Dictation>> {
        return dictationsListRepository.getDictationsByEnglishLevel(englishLevel.name)
    }
}