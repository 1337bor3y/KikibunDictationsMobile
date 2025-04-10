package com.bor3y.kikibundictations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bor3y.dictations_list.presentation.DictationsListScreen
import com.bor3y.kikibundictations.ui.theme.KikibunDictationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KikibunDictationsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val errorMessage = remember { mutableStateOf<String?>(null) }

                LaunchedEffect(errorMessage.value) {
                    errorMessage.value?.let {
                        snackbarHostState.showSnackbar(
                            message = it,
                            duration = SnackbarDuration.Long
                        )
                        errorMessage.value = null
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackbarHostState) { data ->
                            Snackbar(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(8.dp),
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                shape = RoundedCornerShape(16.dp),
                                content = {
                                    Text(
                                        text = data.visuals.message,
                                        fontSize = 16.sp
                                    )
                                },
                                action = {
                                    IconButton(onClick = { data.dismiss() }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.snackbar_close_content_description),
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    }
                ) { padding ->
                    DictationsListScreen(
                        modifier = Modifier.padding(padding),
                        showError = { message ->
                            errorMessage.value = message
                        }
                    )
//                    CameraPreviewScreen(modifier = Modifier.padding(padding)) { recognizedText ->
//                        Log.d("RecognizedText", recognizedText)
//                    }
                }
            }
        }
    }
}
