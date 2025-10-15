package com.bor3y.textrecognition.presentation

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bor3y.textrecognition.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(
    modifier: Modifier = Modifier,
    viewModel: TextRecognitionViewModel = hiltViewModel(),
    onTextRecognized: (String) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(
            modifier = modifier,
            state = state,
            onEvent = viewModel::onEvent,
            onTextRecognized = onTextRecognized
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
    onTextRecognized: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onEvent(
                    TextRecognitionEvent.OnGalleryImageSelected(
                        it.toBitmap(context)
                    )
                )
            }
        }
    )

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
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        state.surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
                modifier = modifier
            )
        }

        IconButton(
            modifier = Modifier
                .align(if (isLandscape()) Alignment.BottomEnd else Alignment.BottomStart)
                .padding(24.dp),
            onClick = {
                pickImageLauncher.launch("image/*")
            }
        ) {
            Icon(
                modifier = Modifier
                    .size(64.dp),
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = stringResource(R.string.choose_from_gallery),
                tint = Color.White
            )
        }

        IconButton(
            modifier = Modifier
                .align(if (isLandscape()) Alignment.CenterEnd else Alignment.BottomCenter)
                .padding(16.dp)
                .size(64.dp)
                .background(Color.White, shape = CircleShape),
            onClick = {
                onEvent(TextRecognitionEvent.TakePhoto)
            }
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = stringResource(R.string.take_photo_button_text)
            )
        }

        state.capturedImage?.let { bitmap ->
            ImagePreviewScreen(
                imageBitmap = bitmap.asImageBitmap(),
                state = state,
                onEvent = onEvent,
                onTextRecognized = onTextRecognized
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(70.dp),
                color = colorResource(R.color.circular_progress_indicator_color),
                strokeWidth = 8.dp
            )
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

private fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
