package com.bor3y.textrecognition.domain.use_case

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.bor3y.textrecognition.domain.repository.TextRecognizerRepository
import javax.inject.Inject

class GetTextFromImageUseCase @Inject constructor(
    private val recognizerRepository: TextRecognizerRepository
) {
    operator fun invoke(
        imageBitmap: Bitmap,
        onTextRecognized: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

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