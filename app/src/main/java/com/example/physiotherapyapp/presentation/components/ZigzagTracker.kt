package com.example.physiotherapyapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.physiotherapyapp.data.model.SensorReading
import kotlin.math.*

@Composable
fun ZigzagTracker(
    sensorData: SensorReading?,
    targetAngle: Float,
    modifier: Modifier = Modifier,
    isCorrect: Boolean = false
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (sensorData != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    
    val currentAngle = sensorData?.angle ?: 0f
    val accuracy = calculateAccuracy(currentAngle, targetAngle)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hareket Takibi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            AccuracyIndicator(accuracy = accuracy, isCorrect = isCorrect)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Zigzag Visualization
        ZigzagCanvas(
            currentAngle = currentAngle,
            targetAngle = targetAngle,
            progress = animatedProgress,
            isCorrect = isCorrect,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Angle Information
        AngleDisplay(
            currentAngle = currentAngle,
            targetAngle = targetAngle,
            accuracy = accuracy
        )
    }
}

@Composable
fun ZigzagCanvas(
    currentAngle: Float,
    targetAngle: Float,
    progress: Float,
    isCorrect: Boolean,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val correctColor = Color.Green
    val incorrectColor = Color.Red
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    
    // Animation for zigzag movement
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        
        // Draw background zigzag pattern
        drawZigzagBackground(width, height, backgroundColor)
        
        // Draw target line
        drawTargetLine(targetAngle, width, height, primaryColor.copy(alpha = 0.5f))
        
        // Draw current position zigzag
        val currentColor = when {
            isCorrect -> correctColor
            abs(currentAngle - targetAngle) < 10f -> Color.Yellow
            else -> incorrectColor
        }
        
        drawCurrentZigzag(
            currentAngle = currentAngle,
            width = width,
            height = height,
            color = currentColor,
            animatedOffset = animatedOffset,
            progress = progress
        )
        
        // Draw accuracy zone
        drawAccuracyZone(targetAngle, width, height, primaryColor.copy(alpha = 0.2f))
    }
}

private fun DrawScope.drawZigzagBackground(width: Float, height: Float, color: Color) {
    val path = Path()
    val segmentWidth = width / 20
    val amplitude = height * 0.1f
    val centerY = height / 2
    
    path.moveTo(0f, centerY)
    
    for (i in 0..20) {
        val x = i * segmentWidth
        val y = centerY + amplitude * sin(i * PI / 2).toFloat()
        path.lineTo(x, y)
    }
    
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = 2.dp.toPx())
    )
}

private fun DrawScope.drawTargetLine(
    targetAngle: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val centerY = height / 2
    val targetY = centerY + (targetAngle / 90f) * (height * 0.3f)
    
    drawLine(
        color = color,
        start = Offset(0f, targetY),
        end = Offset(width, targetY),
        strokeWidth = 3.dp.toPx()
    )
}

private fun DrawScope.drawCurrentZigzag(
    currentAngle: Float,
    width: Float,
    height: Float,
    color: Color,
    animatedOffset: Float,
    progress: Float
) {
    val path = Path()
    val segmentWidth = width / 20
    val centerY = height / 2
    val currentY = centerY + (currentAngle / 90f) * (height * 0.3f)
    val amplitude = height * 0.05f
    
    path.moveTo(-segmentWidth * animatedOffset, currentY)
    
    for (i in 0..21) {
        val x = (i * segmentWidth) - (segmentWidth * animatedOffset)
        if (x > width) break
        if (x < 0) continue
        
        val zigzagOffset = amplitude * sin((i + animatedOffset * 4) * PI / 2).toFloat()
        val y = currentY + zigzagOffset
        path.lineTo(x, y)
    }
    
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = 4.dp.toPx() * progress,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    )
}

private fun DrawScope.drawAccuracyZone(
    targetAngle: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val centerY = height / 2
    val targetY = centerY + (targetAngle / 90f) * (height * 0.3f)
    val zoneHeight = height * 0.1f
    
    drawRect(
        color = color,
        topLeft = Offset(0f, targetY - zoneHeight / 2),
        size = Size(width, zoneHeight)
    )
}

@Composable
fun AccuracyIndicator(
    accuracy: Float,
    isCorrect: Boolean
) {
    val color = when {
        isCorrect -> Color.Green
        accuracy > 80f -> Color.Yellow
        else -> Color.Red
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${accuracy.toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun AngleDisplay(
    currentAngle: Float,
    targetAngle: Float,
    accuracy: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AngleCard(
            title = "Mevcut",
            angle = currentAngle,
            color = MaterialTheme.colorScheme.primary
        )
        AngleCard(
            title = "Hedef",
            angle = targetAngle,
            color = MaterialTheme.colorScheme.secondary
        )
        AngleCard(
            title = "Doğruluk",
            angle = accuracy,
            color = if (accuracy > 80f) Color.Green else Color.Red,
            suffix = "%"
        )
    }
}

@Composable
fun AngleCard(
    title: String,
    angle: Float,
    color: Color,
    suffix: String = "°"
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${angle.toInt()}$suffix",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

private fun calculateAccuracy(currentAngle: Float, targetAngle: Float): Float {
    val difference = abs(currentAngle - targetAngle)
    val maxDifference = 90f // Maximum possible difference
    return ((maxDifference - difference) / maxDifference * 100f).coerceIn(0f, 100f)
}

