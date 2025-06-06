package com.bor3y.dictations_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bor3y.dictations_list.R
import com.bor3y.dictations_list.domain.model.Dictation
import com.bor3y.dictations_list.domain.model.EnglishLevel
import com.bor3y.dictations_list.presentation.component.DictationGuidance
import com.bor3y.dictations_list.presentation.component.DictationItem

@Composable
fun DictationsListScreen(
    modifier: Modifier = Modifier,
    viewModel: DictationsListViewModel = hiltViewModel(),
    showError: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val dictationsPaging = state.dictationPagingData.collectAsLazyPagingItems()
    val dictations = dictationsPaging.itemSnapshotList.items.filter {
        !state.hideCompleted || !it.isCompleted
    }

    LaunchedEffect(key1 = dictationsPaging.loadState) {
        if (dictationsPaging.loadState.refresh is LoadState.Error) {
            showError("Error: ${(dictationsPaging.loadState.refresh as LoadState.Error).error.message}")
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        DictationsListContent(
            state = state,
            dictations = dictations,
            pagingState = dictationsPaging.loadState,
            onEvent = onEvent
        )
    }
}

@Composable
private fun DictationsListContent(
    state: DictationsListState,
    dictations: List<Dictation>,
    pagingState: CombinedLoadStates,
    onEvent: (DictationsListEvent) -> Unit
) {
    val dailyDictations = dictations.filter { it.isNew }
    val allDictations = dictations.filter { !it.isNew }

    when {
        pagingState.refresh is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        dictations.isEmpty() -> {
            EmptyDictationsState()
        }

        else -> {
            EnglishLevelDropdown(
                selectedEnglishLevel = state.englishLevel,
                onEnglishLevelSelected = { onEvent(DictationsListEvent.SelectEnglishLevel(it)) }
            )

            DictationsListColumn(
                dailyDictations = dailyDictations,
                allDictations = allDictations,
                hideCompleted = state.hideCompleted,
                pagingState = pagingState,
                onToggleHideCompleted = { onEvent(DictationsListEvent.ToggleHideCompleted) }
            )
        }
    }
}

@Composable
private fun EmptyDictationsState() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.dictations_empty_text),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        DictationGuidance()
    }
}

@Composable
fun EnglishLevelDropdown(
    modifier: Modifier = Modifier,
    selectedEnglishLevel: EnglishLevel,
    onEnglishLevelSelected: (EnglishLevel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EnglishLevelIcon(
                modifier = Modifier
                    .size(40.dp),
                levelName = selectedEnglishLevel.name,
                boxColor = Color.Black,
                textColor = Color.White
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.english_level_dropdown_text),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Text(
                    text = selectedEnglishLevel.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.dropdown_arrow_icon_content_description),
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            EnglishLevel.entries.forEach { level ->
                DropdownMenuItem(
                    trailingIcon = {
                        EnglishLevelIcon(
                            modifier = Modifier
                                .size(40.dp),
                            levelName = level.name,
                            boxColor = Color.White,
                            textColor = Color.Black
                        )
                    },
                    text = {
                        Text(level.description)
                    },
                    onClick = {
                        onEnglishLevelSelected(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EnglishLevelIcon(
    modifier: Modifier = Modifier,
    levelName: String,
    boxColor: Color,
    textColor: Color
) {
    Box(
        modifier = modifier.background(boxColor, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = levelName,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DictationsListColumn(
    dailyDictations: List<Dictation>,
    allDictations: List<Dictation>,
    hideCompleted: Boolean,
    pagingState: CombinedLoadStates,
    onToggleHideCompleted: () -> Unit
) {
    val hideCompletedText = if (hideCompleted) {
        stringResource(R.string.show_completed_text)
    } else {
        stringResource(R.string.hide_completed_text)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 12.dp, end = 12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        if (dailyDictations.isNotEmpty()) {
            item {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.daily_dictations_text),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(dailyDictations) {
                DictationItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = it.title,
                    isNew = it.isNew,
                    onItemClick = {
                        // TODO: Navigate to detail screen
                    }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.all_dictations_text),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.clickable { onToggleHideCompleted() },
                    text = hideCompletedText,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
            }
        }

        items(allDictations) {
            DictationItem(
                modifier = Modifier.fillMaxWidth(),
                title = it.title,
                isNew = it.isNew,
                onItemClick = {
                    // TODO: Navigate to detail screen
                }
            )
        }

        item {
            if (pagingState.append is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}