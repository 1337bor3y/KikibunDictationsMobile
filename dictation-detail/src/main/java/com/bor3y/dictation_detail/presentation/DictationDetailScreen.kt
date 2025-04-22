package com.bor3y.dictation_detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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
        modifier = modifier,
        dictationDetail = dictationDetail
    )
}

@Composable
fun DictationDetailContent(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail
) {
    DictationTitle()
}

@Preview(
    showBackground = true
)
@Composable
fun DictationTitle(
    modifier: Modifier = Modifier,
    title: String = "My Trip to Italy",
    englishLevelName: String = "B1"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Row {
                Card(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.english_level_card_color)
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = englishLevelName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "English Level",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
        OutlinedButton(
            onClick = {
                // TODO: Share dictation
            },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.outlinedButtonColors().copy(
                contentColor = Color.Gray
            )
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            Text(text = "Share")
        }
    }
}