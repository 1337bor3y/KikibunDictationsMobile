package com.bor3y.dictation_detail.presentation

import android.media.MediaPlayer
import android.net.Uri
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.bor3y.dictation_detail.R
import com.bor3y.dictation_detail.domain.model.DictationDetail
import kotlinx.coroutines.delay
import java.io.File
import java.util.Locale

@Composable
fun DictationDetailScreen(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail
) {
    DictationDetailContent(
        modifier = modifier,
        dictationDetail = dictationDetail
    )
}

@Composable
fun DictationDetailContent(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail
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
            audioFileNormal = dictationDetail.audioFileNormal,
            audioFileDictation = dictationDetail.audioFileDictation,
        )
        Transcription()
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
    audioFileNormal: String,
    audioFileDictation: String
) {
    Column(
        modifier = modifier
    ) {
        var isNormalSpeed by rememberSaveable {
            mutableStateOf(true)
        }

        AudioPlaybackSpeed {
            isNormalSpeed = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        AudioPlayerCard(
            isNormalSpeed = isNormalSpeed,
            audioFileNormal = audioFileNormal,
            audioFileDictation = audioFileDictation
        )
    }
}

@Composable
fun AudioPlaybackSpeed(
    modifier: Modifier = Modifier,
    setIsNormalSpeed: (Boolean) -> Unit
) {
    var isNormalSpeed by rememberSaveable {
        mutableStateOf(true)
    }

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
                    isNormalSpeed = true
                    setIsNormalSpeed(true)
                    /* TODO: Set Normal Speed audio */
                },
                icon = Icons.AutoMirrored.Outlined.VolumeUp,
                isSelected = isNormalSpeed
            )
            Spacer(modifier = Modifier.width(4.dp))
            PlaybackButton(
                modifier = Modifier.weight(1f, true),
                label = "Dictation Speed",
                onClick = {
                    isNormalSpeed = false
                    setIsNormalSpeed(false)
                    /* TODO: Set Dictation Speed audio */
                },
                icon = Icons.Default.Headset,
                isSelected = !isNormalSpeed
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
    isNormalSpeed: Boolean,
    audioFileNormal: String,
    audioFileDictation: String
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
                filePath = if (isNormalSpeed) audioFileNormal else audioFileDictation
            )
        }
    }
}

@Composable
fun CustomAudioPlayer(
    filePath: String
) {
    val context = LocalContext.current
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var currentPosition by rememberSaveable { mutableLongStateOf(0L) }
    var duration by rememberSaveable { mutableLongStateOf(0L) }

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    LaunchedEffect(filePath) {
        mediaPlayer?.release()
        val player = MediaPlayer().apply {
            setDataSource(context, Uri.fromFile(File(filePath)))
            prepare()
            duration = this.duration.toLong()

            setOnCompletionListener {
                seekTo(0)
                currentPosition = 0L
                isPlaying = false
            }
        }
        mediaPlayer = player
        currentPosition = 0L
        duration = player.duration.toLong()
        isPlaying = false
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            mediaPlayer?.let {
                currentPosition = it.currentPosition.toLong()
            }
            delay(1000L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    isPlaying = false
                } else {
                    it.start()
                    isPlaying = true
                }
            }
        }) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatTime(currentPosition), fontSize = 12.sp)
                Text(formatTime(duration), fontSize = 12.sp)
            }

            Slider(
                value = currentPosition.toFloat().coerceAtMost(duration.toFloat()),
                onValueChange = {
                    currentPosition = it.toLong()
                    mediaPlayer?.seekTo(it.toInt())
                },
                valueRange = 0f..duration.toFloat(),
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
    modifier: Modifier = Modifier
) {
    var typedText by rememberSaveable {
        mutableStateOf("")
    }

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
            value = typedText,
            onValueChange = { typedText = it },
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
            enabled = typedText.isNotBlank(),
            onClick = {
                // TODO: Check the Dictation correctness
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
}