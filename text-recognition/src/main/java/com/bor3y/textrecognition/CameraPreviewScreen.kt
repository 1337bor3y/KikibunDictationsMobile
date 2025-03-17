package com.bor3y.textrecognition

import android.content.res.Configuration
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: TextRecognitionViewModel = viewModel(),
    onTextRecognized: (String) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val state by viewModel.state.collectAsStateWithLifecycle()

    state.recognizedText?.let(onTextRecognized)

    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(
            modifier = modifier,
            state = state,
            onEvent = viewModel::onEvent
        )
    } else {
        CameraPermissionRequest(
            modifier = modifier,
            cameraPermissionState = cameraPermissionState
        )
    }
}

@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    state: TextRecognitionState,
    onEvent: (TextRecognitionEvent) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(lifecycleOwner) {
        onEvent(
            TextRecognitionEvent.BindToCamera(
                context.applicationContext,
                lifecycleOwner
            )
        )
    }

    LaunchedEffect(state.error) {
        state.error?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            state.surfaceRequest?.let { request ->
                CameraXViewfinder(
                    surfaceRequest = request,
                    modifier = modifier
                )
            }

            IconButton(
                modifier = Modifier
                    .align(if (isLandscape()) Alignment.CenterEnd else Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape),
                onClick = {
                    onEvent(TextRecognitionEvent.TakePhoto)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = stringResource(R.string.take_photo_button_text)
                )
            }

            state.capturedImage?.let { bitmap ->
                ImagePreviewScreen(
                    imageBitmap = bitmap.asImageBitmap(),
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun CameraPermissionRequest(
    modifier: Modifier,
    cameraPermissionState: PermissionState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .widthIn(max = 480.dp)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
            stringResource(R.string.rationale_camera_permission)
        } else {
            stringResource(R.string.first_time_camera_permission_ask)
        }
        Text(textToShow, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
            Text(stringResource(R.string.permission_request_button_text))
        }
    }
}