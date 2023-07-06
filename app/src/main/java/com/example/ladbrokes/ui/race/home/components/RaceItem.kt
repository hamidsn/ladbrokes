package com.example.ladbrokes.ui.race.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ladbrokes.R
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.util.EXPIRED_DISPLAY_TIME
import com.example.ladbrokes.util.MILLI_SECONDS
import com.example.ladbrokes.util.Utility.formatSeconds
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.8f

@Composable
fun RaceItem(
    modifier: Modifier = Modifier,
    item: Race,
    onItemClicked: (Int) -> Unit,
    textStyle: TextStyle,
    targetTextSizeHeight: TextUnit = textStyle.fontSize,
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }
    Row(
        modifier = modifier
            .clickable { onItemClicked(item.hashCode()) }
            .padding(start = 6.dp, top = 12.dp, bottom = 12.dp)
    ) {
        RaceTimerContainer(modifier = Modifier.size(64.dp)) {
            CountDownIndicator(
                modifier = Modifier.size(64.dp),
                item.seconds, 55, 1,
                textStyle = TextStyle(fontSize = 12.sp)
            )
        }
        Spacer(Modifier.width(20.dp))
        BoxWithConstraints {
            if (maxWidth < 400.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Race number: ${item.race_number}",
                        overflow = TextOverflow.Ellipsis,
                        fontSize = textSize,
                        onTextLayout = { textLayoutResult ->
                            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
                            }
                        }
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = item.meeting_name,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Race number: ${item.race_number}, ",
                        overflow = TextOverflow.Ellipsis,
                        fontSize = textSize,
                        onTextLayout = { textLayoutResult ->
                            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
                            }
                        }
                        // style = MaterialTheme.typography.h6
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = item.meeting_name,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }

        Divider(modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
fun RaceTimerContainer(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Surface(modifier.aspectRatio(1f), RoundedCornerShape(4.dp)) {
        content()
    }
}

@Composable
fun CountDownIndicator(
    modifier: Modifier = Modifier,
    time: Long,
    size: Int,
    stroke: Int,
    textStyle: TextStyle,
    targetTextSizeHeight: TextUnit = textStyle.fontSize,
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }
    Column(modifier = modifier) {
        Box {

            CircularProgressIndicatorBackGround(
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = colorResource(R.color.purple_200),
                stroke
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                val key: Long = time
                val secondsLeft: Long by rememberCountdownTimerState(
                    (time - System.currentTimeMillis() / MILLI_SECONDS),
                    key = key
                )

                Text(
                    text = secondsLeft.formatSeconds(),
                    color = Color.Red,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Default,
                    fontSize = textSize,
                    onTextLayout = { textLayoutResult ->
                        val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                        if (maxCurrentLineIndex > 1) {
                            textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun rememberCountdownTimerState(
    initialMillis: Long,
    key: Long,
    step: Long = MILLI_SECONDS
): MutableState<Long> {
//for next recomposes: remember API invalidates the cache and executes the calculation lambda block again.
    val timeLeft = remember(key1 = key) { mutableStateOf(initialMillis) }
    LaunchedEffect(initialMillis, step) {
        while (isActive && timeLeft.value > EXPIRED_DISPLAY_TIME) {
            delay(step)
            timeLeft.value = (timeLeft.value - 1)
        }
        this.cancel()
    }
    return timeLeft
}

@Composable
fun CircularProgressIndicatorBackGround(
    modifier: Modifier = Modifier,
    color: Color,
    stroke: Int
) {
    val style = with(LocalDensity.current) { Stroke(stroke.dp.toPx()) }

    Canvas(modifier = modifier, onDraw = {

        val innerRadius = (size.minDimension - style.width) / 2

        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            topLeft = Offset(
                (size / 2.0f).width - innerRadius,
                (size / 2.0f).height - innerRadius
            ),
            size = androidx.compose.ui.geometry.Size(innerRadius * 2, innerRadius * 2),
            useCenter = false,
            style = style
        )

    })
}

