package com.bor3y.kikibundictations.navigation.screen_route

import com.bor3y.dictations_list.domain.model.Dictation
import kotlinx.serialization.Serializable

@Serializable
sealed class DictationRoutes {

    @Serializable
    data object DictationsList : DictationRoutes()

    @Serializable
    data class DictationScreenDetail(val dictation: Dictation) :
        DictationRoutes()
}