package com.bor3y.kikibundictations.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bor3y.kikibundictations.R

@Composable
fun ConnectivityBanner(
    isOnline: Boolean
) {
    var hideBanner by rememberSaveable { mutableStateOf(false) }
    var wasOfflineDisplayed by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        hideBanner = false
        if (isOnline && wasOfflineDisplayed) {
            kotlinx.coroutines.delay(3000)
            hideBanner = true
        }
    }

    if (!isOnline && !hideBanner) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.offline_mode))
                .height(46.dp)
                .clickable {
                    showDialog = true
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.offline_mode_icon_content_description),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.offline_mode_text),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        wasOfflineDisplayed = true
    } else if (isOnline && wasOfflineDisplayed && !hideBanner) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.reconnected))
                .height(46.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.reconnected_text),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.offline_mode_dialog_title)) },
            text = { Text(stringResource(R.string.offline_mode_dialog_text)) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    hideBanner = true
                }) {
                    Text(stringResource(R.string.offline_mode_dialog_button_text))
                }
            }
        )
    }
}