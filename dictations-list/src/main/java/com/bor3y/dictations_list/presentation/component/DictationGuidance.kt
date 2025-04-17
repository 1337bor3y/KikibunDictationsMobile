package com.bor3y.dictations_list.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bor3y.dictations_list.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun DictationGuidance(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(R.string.tip_screen_top_text),
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Gray
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ListenTipCard()
        Spacer(modifier = Modifier.height(20.dp))
        TypeTipCard()
        Spacer(modifier = Modifier.height(20.dp))
        ResultAnalysis()
        Spacer(modifier = Modifier.height(20.dp))
        AccuracyScoreTipCard()
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ListenTipCard(modifier: Modifier = Modifier) {
    val infiniteTransition =
        rememberInfiniteTransition(label = stringResource(R.string.infinite_transition))
    val soundProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = stringResource(R.string.sound_progress_animation)
    )

    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 11000

                0f at 0 using LinearEasing
                1f at 500 using LinearEasing
                1f at 10500 using LinearEasing
                0f at 11000 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.text_alpha_animation)
    )

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.tip_container_color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.tip_volume_up_color),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp),
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = stringResource(R.string.speaker_icon_content_description),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    modifier = Modifier.alpha(textAlpha),
                    text = stringResource(R.string.tip_screen_listen_text),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    progress = { soundProgress },
                    color = Color.Gray,
                    trackColor = Color.White,
                    drawStopIndicator = { }
                )
            }
        }
    }
}

@Composable
fun TypeTipCard(modifier: Modifier = Modifier) {
    val infiniteTransition =
        rememberInfiniteTransition(label = stringResource(R.string.infinite_transition))
    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1500

                0.5f at 0 using LinearEasing
                1f at 500 using LinearEasing
                1f at 1000 using LinearEasing
                0.5f at 1500 using LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = stringResource(R.string.text_alpha_animation)
    )

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.tip_screen_hear_text),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.tip_container_color))
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(textAlpha),
                    text = "The quick brown fox over jumps the laisy dog",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun ResultAnalysis(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.tip_screen_result_text),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.tip_container_color))
        ) {
            HighlightedErrorTipText()
        }
    }
}

@Composable
fun HighlightedErrorTipText() {
    var currentAnnotationIndex by rememberSaveable { mutableIntStateOf(0) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    var errorMessage by remember { mutableStateOf<AnnotatedString?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    var popupWidth by rememberSaveable { mutableIntStateOf(0) }
    var popupHeight by rememberSaveable { mutableIntStateOf(0) }

    data class ErrorInfo(val start: Int, val end: Int, val message: AnnotatedString)

    val errors = listOf(
        ErrorInfo(
            start = 20,
            end = 30,
            message = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Should be swapped!\n")
                pop()
                append("Correct: ")
                pushStyle(
                    SpanStyle(
                        background = colorResource(R.color.tip_screen_green),
                        color = Color.White,
                    )
                )
                append("jumps over")
                pop()
            }
        ),
        ErrorInfo(
            start = 35,
            end = 40,
            message = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Minor mistake:\n")
                pop()
                append("Correct: ")
                pushStyle(
                    SpanStyle(
                        background = colorResource(R.color.tip_screen_green),
                        color = Color.White
                    )
                )
                append("lazy")
                pop()
            }
        ),
        ErrorInfo(
            start = 45,
            end = 52,
            message = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Missing word!\n")
                pop()
                append("This word is missing in your text.")
            }
        )
    )

    val annotatedText = buildAnnotatedString {
        append("The quick brown fox ")
        pushStyle(SpanStyle(background = Color(0xFFFFFF00), color = Color.Black))
        append("over jumps")
        pop()
        append(" the ")
        pushStyle(SpanStyle(background = Color(0xFFCCCCFF), color = Color.Black))
        append("laisy")
        pop()
        append(" dog ")
        pushStyle(SpanStyle(background = Color(0xFFFF6666), color = Color.White))
        append("quickly")
        pop()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = annotatedText,
            style = MaterialTheme.typography.bodyLarge,
            onTextLayout = { layoutResult -> textLayoutResult.value = layoutResult }
        )

        errorMessage?.let { message ->
            Box(modifier = Modifier
                .absoluteOffset {
                    IntOffset(
                        popupPosition.x.roundToInt(),
                        popupPosition.y.roundToInt()
                    )
                }
                .onGloballyPositioned {
                    popupWidth = it.size.width
                    popupHeight = it.size.height
                }
                .widthIn(max = 300.dp)
                .background(Color.Black, RoundedCornerShape(8.dp))
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .padding(16.dp)
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 16.sp
                )
            }
        }

        LaunchedEffect(errors) {
            while (true) {
                val error = errors[currentAnnotationIndex]
                textLayoutResult.value?.let { layoutResult ->
                    val startBox = layoutResult.getBoundingBox(error.start)
                    val endBox = layoutResult.getBoundingBox(error.end - 1)

                    val centerX = (startBox.left + endBox.right) / 2
                    val centerY = (startBox.top + startBox.bottom) / 2

                    val safePopupWidth =
                        if (popupWidth > 0) popupWidth else with(density) { 300.dp.roundToPx() }
                    val safePopupHeight =
                        if (popupHeight > 0) popupHeight else with(density) { 100.dp.roundToPx() }

                    val x = ((centerX - safePopupWidth / 4).coerceIn(
                        0f,
                        screenWidthPx - safePopupWidth
                    ))
                    val y = (centerY - safePopupHeight - 40)

                    popupPosition = Offset(x, y)
                    errorMessage = error.message
                }

                delay(2000)
                currentAnnotationIndex = (currentAnnotationIndex + 1) % errors.size
            }
        }
    }
}

@Composable
fun AccuracyScoreTipCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.tip_container_color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.tip_screen_accuracy_text),
                    style = MaterialTheme.typography.bodyLarge
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(R.color.tip_screen_green),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                        ),
                        text = stringResource(R.string.tip_screen_accuracy_number),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                progress = { 0.9F },
                color = colorResource(R.color.tip_screen_green),
                trackColor = Color.White,
                drawStopIndicator = { }
            )
        }
    }
}