package com.bor3y.textrecognition.domain.use_case

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.bor3y.textrecognition.domain.model.Resource
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTextFromImageUseCase @Inject constructor(
    private val recognizerRepository: TextRecognizerRepository
) {
    operator fun invoke(
        imageBitmap: Bitmap,
        framePosition: Offset,
        frameSize: Size,
        screenSize: Size
    ): Flow<Resource<String>> =
        flow {
            try {
                emit(Resource.Loading())
                val croppedImage = cropBitmap(
                    imageBitmap = imageBitmap,
                    framePosition = framePosition,
                    frameSize = frameSize,
                    screenSize = screenSize
                )
                val text = recognizerRepository.getTextFromImage(croppedImage)
                emit(Resource.Success(text))
            } catch (exception: Exception) {
                emit(
                    Resource.Error(
                        "Failed to analyze the image: " +
                                (exception.localizedMessage ?: "Unknown error")
                    )
                )
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