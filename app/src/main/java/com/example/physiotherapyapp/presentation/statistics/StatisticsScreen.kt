package com.example.physiotherapyapp.presentation.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.*

@Composable
fun StatisticsScreen(navController: NavController) {
    var selectedPeriod by remember { mutableStateOf("30 Gün") }
    
    val periods = listOf("7 Gün", "30 Gün", "3 Ay", "1 Yıl")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "İstatistikler",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "İlerlemenizi takip edin ve analiz edin",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // Period Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(periods) { period ->
                    FilterChip(
                        onClick = { selectedPeriod = period },
                        label = { Text(period) },
                        selected = selectedPeriod == period
                    )
                }
            }
        }
        
        item {
            // Summary Cards
            StatisticsSummaryCards()
        }
        
        item {
            // Progress Chart
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "İlerleme Grafiği",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Son $selectedPeriod içindeki performansınız",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    SimpleLineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
        
        item {
            // Body Part Distribution
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Vücut Bölgesi Dağılımı",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val bodyParts = listOf(
                        "Kol" to 30f to Color(0xFF4CAF50),
                        "Bacak" to 25f to Color(0xFF2196F3),
                        "Omuz" to 20f to Color(0xFFFF9800),
                        "Sırt" to 15f to Color(0xFF9C27B0),
                        "Diz" to 10f to Color(0xFFF44336)
                    )
                    
                    bodyParts.forEach { (name, data) ->
                        val (percentage, color) = data
                        BodyPartProgressItem(name, percentage, color)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticsSummaryCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatisticCard(
            title = "Toplam Seans",
            value = "24",
            icon = Icons.Default.PlayArrow,
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )
        
        StatisticCard(
            title = "Ortalama Doğruluk",
            value = "85%",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF2196F3),
            modifier = Modifier.weight(1f)
        )
    }
    
    Spacer(modifier = Modifier.height(12.dp))
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatisticCard(
            title = "Toplam Puan",
            value = "1250",
            icon = Icons.Default.EmojiEvents,
            color = Color(0xFFFFD700),
            modifier = Modifier.weight(1f)
        )
        
        StatisticCard(
            title = "Seviye",
            value = "5",
            icon = Icons.Default.Star,
            color = Color(0xFFFF9800),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SimpleLineChart(modifier: Modifier = Modifier) {
    val data = listOf(75f, 82f, 88f, 85f, 91f, 87f, 89f)
    
    Canvas(modifier = modifier) {
        val maxValue = data.maxOrNull() ?: 100f
        val minValue = data.minOrNull() ?: 0f
        val range = maxValue - minValue
        
        val stepX = size.width / (data.size - 1)
        val path = Path()
        
        data.forEachIndexed { index, point ->
            val x = index * stepX
            val y = size.height - ((point - minValue) / range * size.height)
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            
            // Draw points
            drawCircle(
                color = Color.Blue,
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )
        }
        
        // Draw line
        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun BodyPartProgressItem(
    name: String,
    percentage: Float,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(60.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
