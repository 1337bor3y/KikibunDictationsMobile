package com.bor3y.textrecognition

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun ImagePreviewScreen(
    imageBitmap: ImageBitmap,
    onClose: () -> Unit
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Captured Photo",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        ResizableFrame(
            rectSize = Size(300f, 200f),
            rectPosition = Offset(100f, 200f),
            handleSize = with(density) {20.dp.toPx()},
            frameColor = colorResource(id = R.color.camera_frame_color),
            handlesColor = colorResource(id = R.color.frame_handles_color)
        )

        IconButton(
            onClick = { onClose() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.White, shape = CircleShape),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close image preview"
            )
        }
    }
}

@Composable
fun ResizableFrame(
    rectSize: Size,
    rectPosition: Offset,
    handleSize: Float,
    frameColor: Color,
    handlesColor: Color
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
                        mutableRectPosition, // Top-left
                        mutableRectPosition + Offset(mutableRectSize.width, 0f), // Top-right
                        mutableRectPosition + Offset(0f, mutableRectSize.height), // Bottom-left
                        mutableRectPosition + Offset(mutableRectSize.width, mutableRectSize.height) // Bottom-right
                    )

                    resizingCorner = corners.find {
                        touchPoint.x in (it.x - handleSize)..(it.x + handleSize) &&
                                touchPoint.y in (it.y - handleSize)..(it.y + handleSize)
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    resizingCorner?.let {
                        val newWidth =
                            (mutableRectSize.width + if (it.x == mutableRectPosition.x) -dragAmount.x else dragAmount.x).coerceAtLeast(
                                50f
                            )
                        val newHeight =
                            (mutableRectSize.height + if (it.y == mutableRectPosition.y) -dragAmount.y else dragAmount.y).coerceAtLeast(
                                50f
                            )
                        mutableRectSize = Size(newWidth, newHeight)

                        if (it.x == mutableRectPosition.x) mutableRectPosition =
                            mutableRectPosition.copy(x = mutableRectPosition.x + dragAmount.x)
                        if (it.y == mutableRectPosition.y) mutableRectPosition =
                            mutableRectPosition.copy(y = mutableRectPosition.y + dragAmount.y)
                    } ?: run {
                        mutableRectPosition += dragAmount
                    }
                },
                onDragEnd = {
                    resizingCorner = null
                }
            )
        }
    ) {
        drawRect(
            color = frameColor,
            topLeft = mutableRectPosition,
            size = mutableRectSize,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
        )

        val cornerOffsets = listOf(
            mutableRectPosition, // Top-left
            mutableRectPosition + Offset(mutableRectSize.width, 0f), // Top-right
            mutableRectPosition + Offset(0f, mutableRectSize.height), // Bottom-left
            mutableRectPosition + Offset(mutableRectSize.width, mutableRectSize.height) // Bottom-right
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