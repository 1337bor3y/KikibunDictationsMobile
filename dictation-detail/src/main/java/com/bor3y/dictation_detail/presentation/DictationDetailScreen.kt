package com.bor3y.dictation_detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bor3y.dictation_detail.domain.model.DictationDetail

@Composable
fun DictationDetailScreen(
    modifier: Modifier = Modifier,
    dictationDetail: DictationDetail
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = dictationDetail.toString())
    }
}