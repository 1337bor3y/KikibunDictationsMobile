package com.bor3y.dictation_detail.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bor3y.dictation_detail.R
import com.bor3y.dictation_detail.domain.model.DictationDetail
import java.util.Locale

@Composable
fun DictationDetailScreen(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail,
    viewModel: DictationDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val context = LocalContext.current
    onEvent(DictationDetailEvent.SetTranscription(dictationDetail.text))

    LaunchedEffect(dictationDetail.id) {
        onEvent(
            DictationDetailEvent.SetFilePaths(
                audioFileDictation = dictationDetail.audioFileDictation,
                audioFileNormal = dictationDetail.audioFileNormal,
                context = context
            )
        )
    }

    DictationDetailContent(
        modifier = modifier,
        dictationDetail = dictationDetail,
        state = state,
        onEvent = onEvent
    )
}

@Composable
fun DictationDetailContent(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail,
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DictationTitle(
            title = dictationDetail.title,
            englishLevelName = dictationDetail.englishLevelName
        )
        AudioPlayback(
            state = state,
            onEvent = onEvent,
        )
        Transcription(
            state = state,
            onEvent = onEvent,
        )
    }
}

@Composable
fun DictationTitle(
    modifier: Modifier = Modifier,
    title: String,
    englishLevelName: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.light_grey)
                    )
                ) {
                    Text(
                        text = englishLevelName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "English Level",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedButton(
            onClick = { /* TODO: Share dictation */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors().copy(
                contentColor = Color.Gray
            )
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share dictation"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Share")
        }
    }
}

@Composable
fun AudioPlayback(
    modifier: Modifier = Modifier,
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AudioPlaybackSpeed(
            state = state,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.height(16.dp))
        AudioPlayerCard(
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
fun AudioPlaybackSpeed(
    modifier: Modifier = Modifier,
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Audio Playback:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlaybackButton(
                modifier = Modifier.weight(1f, true),
                label = "Normal Speed",
                onClick = {
                    onEvent(DictationDetailEvent.ChangePlaybackSpeed(context))
                },
                icon = Icons.AutoMirrored.Outlined.VolumeUp,
                isSelected = state.isNormalSpeed
            )
            Spacer(modifier = Modifier.width(4.dp))
            PlaybackButton(
                modifier = Modifier.weight(1f, true),
                label = "Dictation Speed",
                onClick = {
                    onEvent(DictationDetailEvent.ChangePlaybackSpeed(context))
                },
                icon = Icons.Default.Headset,
                isSelected = !state.isNormalSpeed
            )
        }
    }
}

@Composable
fun PlaybackButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Black else colorResource(R.color.light_grey),
            contentColor = if (isSelected) Color.White else Color.Gray
        )
    ) {
        Icon(imageVector = icon, contentDescription = "$label icon")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label)
    }
}

@Composable
fun AudioPlayerCard(
    modifier: Modifier = Modifier,
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AudioFile,
                    contentDescription = "File open"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Listen first to understand the content. Do not write yet.",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            CustomAudioPlayer(
                state = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CustomAudioPlayer(
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            onEvent(DictationDetailEvent.TogglePlayPause)
        }) {
            Icon(
                imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatTime(state.currentPosition), fontSize = 12.sp)
                Text(formatTime(state.duration), fontSize = 12.sp)
            }

            Slider(
                value = state.currentPosition.toFloat().coerceAtMost(state.duration.toFloat()),
                onValueChange = {
                    onEvent(DictationDetailEvent.SeekTo(it.toLong()))
                },
                valueRange = 0f..state.duration.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black,
                    activeTrackColor = Color.DarkGray,
                    inactiveTrackColor = Color.Gray,
                )
            )
        }

        IconButton(onClick = {
            // TODO: And menu
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}

@Composable
fun Transcription(
    modifier: Modifier = Modifier,
    state: DictationDetailState,
    onEvent: (DictationDetailEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Your transcription:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 170.dp),
            value = state.typedText,
            onValueChange = {
                onEvent(DictationDetailEvent.SetTypedText(it))
            },
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = "Listen carefully and type what you hear...",
                    color = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                disabledBorderColor = Color.LightGray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Black
            ),
            enabled = state.typedText.isNotBlank(),
            onClick = {
                onEvent(DictationDetailEvent.FindAccuracy)
            }
        ) {
            Text(
                text = "Check My Dictation",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = "Check circle"
            )
        }
    }

    if (state.showAccuracyDialog) {
        AccuracyResultDialog(
            state = state,
            onDismiss = {
                onEvent(DictationDetailEvent.HideAccuracyDialog)
            }
        )
    }
}