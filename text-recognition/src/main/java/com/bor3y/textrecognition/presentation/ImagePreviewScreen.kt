package com.bor3y.textrecognition.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bor3y.textrecognition.R

@Composable
fun ImagePreviewScreen(
    imageBitmap: ImageBitmap,
    state: TextRecognitionState,
    onEvent: (TextRecognitionEvent) -> Unit,
    onTextRecognized: (String) -> Unit
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenSize = Size(
        configuration.screenWidthDp * density.density,
        configuration.screenHeightDp * density.density
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(R.string.taken_image_content_description),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.FillBounds
        )

        ResizableFrame(
            rectSize = state.frameDimensions.size,
            rectPosition = state.frameDimensions.position,
            handleSize = with(density) { 20.dp.toPx() },
            frameColor = colorResource(R.color.camera_frame_color),
            handlesColor = colorResource(R.color.frame_handles_color),
            containerSize = screenSize,
            onFrameChange = { frameSize, framePosition ->
                onEvent(
                    TextRecognitionEvent.UpdateFrameDimensions(
                        frameSize = frameSize,
                        framePosition = framePosition
                    )
                )
            }
        )

        IconButton(
            onClick = { onEvent(TextRecognitionEvent.CloseImagePreview) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close_image_preview_button_content_description)
            )
        }

        Button(
            onClick = {
                onEvent(
                    TextRecognitionEvent.GetTextFromImage(
                        screenSize = screenSize,
                        onTextRecognized = onTextRecognized
                    )
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.analyze_button_text))
        }
    }
}

@Composable
fun ResizableFrame(
    rectSize: Size,
    rectPosition: Offset,
    handleSize: Float,
    frameColor: Color,
    handlesColor: Color,
    containerSize: Size,
    onFrameChange: (frameSize: Size, framePosition: Offset) -> Unit
) {
    var mutableRectSize by remember { mutableStateOf(rectSize) }
    var mutableRectPosition by remember { mutableStateOf(rectPosition) }
    var resizingCorner by remember { mutableStateOf<Offset?>(null) }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { touchPoint ->
                    val corners = listOf(
                        mutableRectPosition,
                        mutableRectPosition + Offset(mutableRectSize.width, 0f),
                        mutableRectPosition + Offset(0f, mutableRectSize.height),
                        mutableRectPosition + Offset(
                            mutableRectSize.width,
                            mutableRectSize.height
                        )
                    )

                    resizingCorner = corners.find {
                        touchPoint.x in (it.x - handleSize)..(it.x + handleSize) &&
                                touchPoint.y in (it.y - handleSize)..(it.y + handleSize)
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    resizingCorner?.let {
                        val isLeft = it.x == mutableRectPosition.x
                        val isTop = it.y == mutableRectPosition.y

                        var newWidth =
                            mutableRectSize.width + (if (isLeft) -dragAmount.x else dragAmount.x)
                        var newHeight =
                            mutableRectSize.height + (if (isTop) -dragAmount.y else dragAmount.y)

                        newWidth = newWidth.coerceAtLeast(50f)
                        newHeight = newHeight.coerceAtLeast(50f)

                        val maxWidth = containerSize.width - mutableRectPosition.x
                        val maxHeight = containerSize.height - mutableRectPosition.y
                        newWidth = newWidth.coerceAtMost(maxWidth)
                        newHeight = newHeight.coerceAtMost(maxHeight)

                        if (isLeft) mutableRectPosition = mutableRectPosition.copy(
                            x = (mutableRectPosition.x + dragAmount.x).coerceAtLeast(0f)
                        )
                        if (isTop) mutableRectPosition = mutableRectPosition.copy(
                            y = (mutableRectPosition.y + dragAmount.y).coerceAtLeast(0f)
                        )

                        mutableRectSize = Size(newWidth, newHeight)
                    } ?: run {
                        val newX = (mutableRectPosition.x + dragAmount.x).coerceIn(
                            0f,
                            containerSize.width - mutableRectSize.width
                        )
                        val newY = (mutableRectPosition.y + dragAmount.y).coerceIn(
                            0f,
                            containerSize.height - mutableRectSize.height
                        )
                        mutableRectPosition = Offset(newX, newY)
                    }
                },
                onDragEnd = {
                    onFrameChange(mutableRectSize, mutableRectPosition)
                    resizingCorner = null
                }
            )
        }
    ) {
        drawRect(
            color = frameColor,
            topLeft = mutableRectPosition,
            size = mutableRectSize,
            style = Stroke(width = 5f)
        )

        val cornerOffsets = listOf(
            mutableRectPosition,
            mutableRectPosition + Offset(mutableRectSize.width, 0f),
            mutableRectPosition + Offset(0f, mutableRectSize.height),
            mutableRectPosition + Offset(mutableRectSize.width, mutableRectSize.height)
        )

        cornerOffsets.forEach { corner ->
            drawCircle(
                color = handlesColor,
                center = corner,
                radius = handleSize / 2
            )
        }
    }
}