package com.bor3y.textrecognition.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bor3y.textrecognition.domain.model.Resource
import com.bor3y.textrecognition.domain.use_case.GetTextFromImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    private val getTextFromImage: GetTextFromImageUseCase
) : ViewModel() {
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

            is TextRecognitionEvent.GetTextFromImage -> getTextFromImage(
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

    private fun getTextFromImage(screenSize: Size, onTextRecognized: (String) -> Unit) {
        state.value.capturedImage?.let { capturedImage ->
            getTextFromImage.invoke(
                imageBitmap = capturedImage,
                frameSize = state.value.frameDimensions.size,
                framePosition = state.value.frameDimensions.position,
                screenSize = screenSize
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.errorMessage
                        )
                    }

                    is Resource.Loading -> _state.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        result.data?.let(onTextRecognized)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}