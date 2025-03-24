package com.bor3y.kikibundictations

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bor3y.kikibundictations.ui.theme.KikibunDictationsTheme
import com.bor3y.textrecognition.presentation.CameraPreviewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KikibunDictationsTheme {
                CameraPreviewScreen { recognizedText ->
                    Log.d("RecognizedText", recognizedText)
                }
            }
        }
    }
}
