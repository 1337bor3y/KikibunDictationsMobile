package com.bor3y.kikibundictations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.bor3y.core.network.NetworkMonitor
import com.bor3y.kikibundictations.component.ConnectivityBanner
import com.bor3y.kikibundictations.navigation.nav_host.DictationNavHost
import com.bor3y.kikibundictations.navigation.screen_route.DictationRoutes
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
                val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
                val navController = rememberNavController()

                Scaffold { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        ConnectivityBanner(isOnline = isOnline)
                        DictationNavHost(
                            navController = navController,
                            startDestination = DictationRoutes.DictationsList
                        )
                    }
                }
            }
        }
    }
}