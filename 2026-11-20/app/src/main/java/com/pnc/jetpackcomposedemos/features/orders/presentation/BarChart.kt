package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme


data class Bar(
    val label: String,
    val value: Double
)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCenteredLabel(
    measurer: TextMeasurer,
    text: String,
    style: TextStyle,
    centerX: Float,
    top: Float
) {
    val layout = measurer.measure(text, style)
    drawText(layout, topLeft = Offset(centerX - layout.size.width / 2f, top))
}


// A bar chart drawn directly on a Canvas.
// Tap a bar to select it; tap again to clear the selection.
// [summary] is what a screen reader says about the chart as a whole
@Composable
fun BarChart(
    bars: List<Bar>,
    selectedIndex: Int?,
    onSelect: (Int?) -> Unit,
    summary: String,
    modifier: Modifier = Modifier
) {
    // capture the needed colors from MaterialTheme
    // b/c we will use a lambda function to draw the bars
    // and MaterialTheme can only be accessed within a @Composable
    val barColor = MaterialTheme.colorScheme.primary
    val selectedColor = MaterialTheme.colorScheme.tertiary
    val gridColor = MaterialTheme.colorScheme.outlineVariant

    val labelStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 12.sp
    )
    val textMeasurer = rememberTextMeasurer()

    // we want the bars to grow from 0 height to their natural height
    // when the chart first loads
    val growth = remember { Animatable(0f) }
    LaunchedEffect(bars) {
        growth.snapTo(0f)
        growth.animateTo(1f, animationSpec = tween(durationMillis = 600))
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .semantics {
                contentDescription = summary
                if (selectedIndex != null && selectedIndex in bars.indices) {
                    stateDescription = bars[selectedIndex].label
                }
            }
            .pointerInput(bars, selectedIndex) {
                detectTapGestures { tap ->
                    if (bars.isEmpty()) return@detectTapGestures
                    val index = (tap.x / (size.width.toFloat() / bars.size))
                        .toInt()
                        .coerceIn(0, bars.lastIndex)
                    onSelect(if (index == selectedIndex) null else index)
                }
            }
    ) {
        if (bars.isEmpty()) return@Canvas

        val labelHeight = 20.dp.toPx()
        val chartHeight = size.height - labelHeight
        val slot = size.width / bars.size
        val barWidth = slot * 0.55f
        val maxValue = bars.maxOf { it.value }.coerceAtLeast(1.0)

        // horizontal guidelines at 0, 50, and 100 percent of the tallest bar
        listOf(0f, 0.5f, 1f).forEach { fraction ->
            val y = chartHeight - chartHeight * fraction
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // draw each bar
        bars.forEachIndexed { index, bar ->
            val barHeight = (bar.value / maxValue).toFloat() * chartHeight * growth.value
            // center the bar in its slot
            val left = index * slot + (slot - barWidth) / 2

            drawRoundRect(
                color = if (index == selectedIndex) selectedColor else barColor,
                topLeft = Offset(left, chartHeight - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )

            drawCenteredLabel(
                textMeasurer,
                bar.label,
                labelStyle,
                slot * index + slot / 2f,
                chartHeight + 4.dp.toPx()
            )

        }

        // an outline around the selected bar's slot
        if (selectedIndex != null && selectedIndex in bars.indices) {
            drawRoundRect(
                color = selectedColor,
                topLeft = Offset(slot * selectedIndex, 0f),
                size = Size(slot, chartHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun BarChartPreview() {
    KazooTheme {
        BarChart(
            bars = listOf(Bar("Jan", 1200.0), Bar("Feb", 800.0), Bar("Mar", 2400.0), Bar("Apr", 1700.0)),
            selectedIndex = 2,
            onSelect = {},
            summary = "Orders by month"
        )
    }
}



