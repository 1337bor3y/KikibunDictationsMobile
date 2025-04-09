package com.bor3y.kikibundictations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                DictationsListScreen()
//                CameraPreviewScreen { recognizedText ->
//                    Log.d("RecognizedText", recognizedText)
//                }
            }
        }
    }
}
