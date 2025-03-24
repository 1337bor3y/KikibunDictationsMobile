package com.bor3y.textrecognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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

            is TextRecognitionEvent.UpdateFrameDimensions -> {
                _state.update {
                    it.copy(
                        frameDimensions = FrameDimensions(
                            size = event.frameSize,
                            position = event.framePosition
                        )
                    )
                }
            }

            is TextRecognitionEvent.AnalyzeImage -> analyzeImage(
                event.screenSize,
                event.onTextRecognized
            )
        }
    }

    private fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            var processCameraProvider: ProcessCameraProvider? = null

            try {
                processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
                processCameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    cameraPreviewUseCase,
                    imageCapture
                )
                _state.update {
                    it.copy(
                        error = null
                    )
                }
                awaitCancellation()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Failed to bind the camera: " +
                                (e.message ?: "Unknown error")
                    )
                }
            } finally {
                processCameraProvider?.unbindAll()
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

                    _state.update {
                        it.copy(
                            capturedImage = rotatedBitmap,
                            error = null
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    _state.update {
                        it.copy(
                            error = "Failed to capture the image: " +
                                    (exception.message ?: "Unknown error")
                        )
                    }
                }
            }
        )
    }

    private fun closeImagePreview() {
        _state.update { it.copy(capturedImage = null) }
    }

    private fun analyzeImage(screenSize: Size, onTextRecognized: (String) -> Unit) {
        state.value.capturedImage?.let { capturedImage ->
            val image = InputImage.fromBitmap(
                cropBitmap(
                    imageBitmap = capturedImage,
                    framePosition = state.value.frameDimensions.position,
                    frameSize = state.value.frameDimensions.size,
                    screenSize = screenSize,
                ),
                0
            )
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onTextRecognized(visionText.text)
                    _state.update {
                        it.copy(
                            error = null
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    _state.update {
                        it.copy(
                            error = "Failed to analyze the image: " +
                                    (exception.localizedMessage ?: "Unknown error")
                        )
                    }
                }
        }
    }

    private fun cropBitmap(
        imageBitmap: Bitmap,
        framePosition: Offset,
        frameSize: Size,
        screenSize: Size
    ): Bitmap {
        val scaleX = imageBitmap.width.toFloat() / screenSize.width
        val scaleY = imageBitmap.height.toFloat() / screenSize.height

        val left = (framePosition.x * scaleX).toInt().coerceIn(0, imageBitmap.width - 1)
        val top = (framePosition.y * scaleY).toInt().coerceIn(0, imageBitmap.height - 1)
        val width = (frameSize.width * scaleX).toInt().coerceAtLeast(1)
        val height = (frameSize.height * scaleY).toInt().coerceAtLeast(1)

        val right = (left + width).coerceAtMost(imageBitmap.width)
        val bottom = (top + height).coerceAtMost(imageBitmap.height)

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