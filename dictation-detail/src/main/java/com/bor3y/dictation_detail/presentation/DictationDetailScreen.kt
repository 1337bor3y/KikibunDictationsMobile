package com.bor3y.dictation_detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bor3y.dictation_detail.R
import com.bor3y.dictation_detail.domain.model.DictationDetail

@Composable
fun DictationDetailScreen(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail
) {
    DictationDetailContent(
        modifier = modifier
    )
}

@Preview(
    showBackground = true
)
@Composable
fun DictationDetailContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DictationTitle(
            title = "My Trip to Italy",
            englishLevelName = "B1"
        )
        AudioPlayback()
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AudioPlaybackSpeed()
    }
}

@Composable
fun AudioPlaybackSpeed(
    modifier: Modifier = Modifier
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
