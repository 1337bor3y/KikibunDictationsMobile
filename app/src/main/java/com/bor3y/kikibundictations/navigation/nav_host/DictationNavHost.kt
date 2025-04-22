package com.bor3y.kikibundictations.navigation.nav_host

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bor3y.dictation_detail.domain.model.DictationDetail
import com.bor3y.dictation_detail.presentation.DictationDetailScreen
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.presentation.DictationsListScreen
import com.bor3y.kikibundictations.navigation.parcelableType
import com.bor3y.kikibundictations.navigation.screen_route.DictationRoutes
import kotlin.reflect.typeOf

@Composable
fun DictationNavHost(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<DictationRoutes.DictationsList> {
            DictationsListScreen(
                onDictationClick = { dictation ->
                    navController.navigate(
                        DictationRoutes.DictationScreenDetail(dictation)
                    )
                }
            )
        }
        composable<DictationRoutes.DictationScreenDetail>(
            typeMap = mapOf(typeOf<Dictation>() to parcelableType<Dictation>())
        ) {
            val dictation =
                it.toRoute<DictationRoutes.DictationScreenDetail>().dictation

            DictationDetailScreen(
                dictationDetail = DictationDetail(
                    id = dictation.id,
                    title = dictation.title,
                    text = dictation.text,
                    audioFileDictation = dictation.audioFileDictation,
                    audioFileNormal = dictation.audioFileNormal,
                    englishLevelName = dictation.englishLevel.name
                )
            )
        }
    }
}