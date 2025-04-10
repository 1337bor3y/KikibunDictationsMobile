package com.bor3y.dictations_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bor3y.dictations_list.domain.model.EnglishLevel
import com.bor3y.dictations_list.presentation.component.DictationItem

@Composable
fun DictationsListScreen(
    modifier: Modifier = Modifier,
    viewModel: DictationsListViewModel = hiltViewModel(),
    showError: (String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val dictations = state.value.dictationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(key1 = dictations.loadState) {
        if (dictations.loadState.refresh is LoadState.Error) {
            showError("Error: ${(dictations.loadState.refresh as LoadState.Error).error.message}")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (dictations.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    EnglishLevelDropdown(
                        selectedEnglishLevel = state.value.englishLevel,
                        onEnglishLevelSelected = { englishLevel ->
                            onEvent(DictationsListEvent.SelectEnglishLevel(englishLevel))
                        }
                    )
                }
                items(dictations.itemCount) { dictationIndex ->
                    dictations[dictationIndex]?.let {
                        DictationItem(
                            modifier = Modifier.fillMaxWidth(),
                            title = it.title,
                            isNew = it.isNew
                        )
                    }
                }
                item {
                    if (dictations.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(12.dp)
                .fillMaxWidth()
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
                    text = "English Level",
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
                contentDescription = "Dropdown Arrow",
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
        modifier = modifier.background(boxColor, shape = CircleShape),
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