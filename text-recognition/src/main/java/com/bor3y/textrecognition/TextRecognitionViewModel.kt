package com.bor3y.textrecognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(TextRecognitionState())
    val state = _state.asStateFlow()

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _state.update { it.copy(surfaceRequest = newSurfaceRequest) }
        }
    }

    private val imageCapture = ImageCapture.Builder().build()

    fun onEvent(event: TextRecognitionEvent) {
        when (event) {
            is TextRecognitionEvent.BindToCamera -> bindToCamera(
                event.appContext,
                event.lifecycleOwner
            )

            TextRecognitionEvent.TakePhoto -> takePhoto()
            TextRecognitionEvent.CloseImagePreview -> closeImagePreview()
            is TextRecognitionEvent.UpdateDimensions -> {
                _state.update {
                    it.copy(
                        dimensions =  Dimensions(
                            frameSize = event.frameSize,
                            framePosition = event.framePosition,
                            screenSize = event.screenSize
                        )
                    )
                }
            }
        }
    }

    private fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
            processCameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                cameraPreviewUseCase,
                imageCapture
            )

            try {
                awaitCancellation()
            } finally {
                processCameraProvider.unbindAll()
            }
        }
    }

    private fun takePhoto() {
        imageCapture.takePicture(
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }

                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    _state.update { it.copy(capturedImage = rotatedBitmap) }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.d("ImageCaptureException", exception.message ?: "Unknown exception")
                }
            }
        )
    }

    private fun closeImagePreview() {
        _state.update { it.copy(capturedImage = null) }
    }

    private fun cropBitmap(
        imageBitmap: Bitmap,
        framePosition: Offset,
        frameSize: Size,
        screenSize: Size
    ): Bitmap {
        val scaleX = imageBitmap.width / screenSize.width
        val scaleY = imageBitmap.height / screenSize.height

        val left = (framePosition.x * scaleX).toInt().coerceIn(0, imageBitmap.width)
        val top = (framePosition.y * scaleY).toInt().coerceIn(0, imageBitmap.height)
        val right =
            ((framePosition.x + frameSize.width) * scaleX).toInt().coerceIn(0, imageBitmap.width)
        val bottom =
            ((framePosition.y + frameSize.height) * scaleY).toInt().coerceIn(0, imageBitmap.height)

        val cropRect = Rect(left, top, right, bottom)

        return Bitmap.createBitmap(
            imageBitmap,
            cropRect.left,
            cropRect.top,
            cropRect.width(),
            cropRect.height()
        )
    }
}