package com.bor3y.kikibundictations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bor3y.core.network.NetworkMonitor
import com.bor3y.dictations_list.presentation.DictationsListScreen
import com.bor3y.kikibundictations.component.ConnectivityBanner
import com.bor3y.kikibundictations.ui.theme.KikibunDictationsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KikibunDictationsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
                val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)

                LaunchedEffect(errorMessage) {
                    errorMessage?.let {
                        snackbarHostState.showSnackbar(
                            message = it,
                            duration = SnackbarDuration.Long
                        )
                        errorMessage = null
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
                    Column(modifier = Modifier.padding(padding)) {
                        ConnectivityBanner(isOnline = isOnline)
                        DictationsListScreen(
                            showError = { message ->
                                errorMessage = message
                            }
                        )
                    }
                }
            }
        }
    }
}