package com.example.physiotherapyapp.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun LineChart(
    data: List<ChartData>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Henüz veri yok",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    
    Canvas(modifier = modifier) {
        val maxValue = data.maxOfOrNull { it.value } ?: 1f
        val minValue = data.minOfOrNull { it.value } ?: 0f
        val range = maxValue - minValue
        
        val stepX = size.width / (data.size - 1).coerceAtLeast(1)
        val path = Path()
        
        data.forEachIndexed { index, point ->
            val x = index * stepX
            val y = size.height - ((point.value - minValue) / range * size.height)
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x * animatedProgress, y)
            }
            
            // Draw points
            drawCircle(
                color = lineColor,
                radius = 6.dp.toPx(),
                center = Offset(x * animatedProgress, y)
            )
        }
        
        // Draw line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Henüz veri yok",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    
    val total = data.sumOf { it.value.toDouble() }.toFloat()
    
    Row(modifier = modifier) {
        // Pie Chart
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .weight(1f)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = minOf(size.width, size.height) / 2 * 0.8f
            
            var startAngle = -90f
            
            data.forEach { slice ->
                val sweepAngle = (slice.value / total) * 360f * animatedProgress
                
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(
                        center.x - radius,
                        center.y - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )
                
                startAngle += sweepAngle
            }
        }
        
        // Legend
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { slice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(2.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(color = slice.color)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = slice.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${((slice.value / total) * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BarChart(
    data: List<BarChartData>,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Henüz veri yok",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    
    val maxValue = data.maxOfOrNull { it.value } ?: 1f
    
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val barWidth = size.width / data.size * 0.7f
            val spacing = size.width / data.size * 0.3f
            
            data.forEachIndexed { index, bar ->
                val barHeight = (bar.value / maxValue) * size.height * animatedProgress
                val x = index * (barWidth + spacing) + spacing / 2
                
                drawRect(
                    color = bar.color,
                    topLeft = Offset(x, size.height - barHeight),
                    size = Size(barWidth, barHeight)
                )
                
                // Draw value on top of bar
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${bar.value.toInt()}%",
                        x + barWidth / 2,
                        size.height - barHeight - 10.dp.toPx(),
                        android.graphics.Paint().apply {
                            color = bar.color.toArgb()
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 12.dp.toPx()
                        }
                    )
                }
            }
        }
        
        // Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { bar ->
                Text(
                    text = bar.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class ChartData(
    val label: String,
    val value: Float
)

data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color
)

data class BarChartData(
    val label: String,
    val value: Float,
    val color: Color
)

