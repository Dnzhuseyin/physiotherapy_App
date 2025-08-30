package com.example.physiotherapyapp.presentation.exercise

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun ExerciseSessionScreen(
    exerciseId: String,
    navController: NavController
) {
    var sessionState by remember { mutableStateOf(SessionState.PREPARATION) }
    var currentScore by remember { mutableStateOf(0) }
    var exerciseProgress by remember { mutableStateOf(0f) }
    var timeRemaining by remember { mutableStateOf(120) }
    var countdown by remember { mutableStateOf(5) }
    
    val exercise = MockExercise(
        id = exerciseId,
        name = "Kol Fleksiyonu",
        description = "Kolunuzu yukarı kaldırın ve indirin",
        duration = 120,
        difficulty = "Başlangıç",
        bodyPart = "Kol",
        points = 15
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header with progress and score
        SessionHeader(
            exerciseName = exercise.name,
            progress = exerciseProgress,
            score = currentScore,
            timeRemaining = timeRemaining
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (sessionState) {
            SessionState.PREPARATION -> {
                PreparationScreen(
                    exercise = exercise,
                    countdown = countdown,
                    onCountdownFinished = { 
                        sessionState = SessionState.ACTIVE
                        timeRemaining = exercise.duration
                    }
                )
                
                LaunchedEffect(countdown) {
                    if (countdown > 0) {
                        delay(1000)
                        countdown--
                    }
                }
            }
            SessionState.ACTIVE -> {
                ActiveExerciseScreen(
                    exercise = exercise,
                    onPauseExercise = { sessionState = SessionState.PAUSED }
                )
                
                LaunchedEffect(timeRemaining) {
                    while (timeRemaining > 0 && sessionState == SessionState.ACTIVE) {
                        delay(1000)
                        timeRemaining--
                        exerciseProgress = (exercise.duration - timeRemaining).toFloat() / exercise.duration
                        currentScore += (1..3).random() // Mock score increase
                    }
                    if (timeRemaining <= 0) {
                        sessionState = SessionState.COMPLETED
                    }
                }
            }
            SessionState.PAUSED -> {
                PausedScreen(
                    onResumeExercise = { sessionState = SessionState.ACTIVE },
                    onEndExercise = { sessionState = SessionState.COMPLETED }
                )
            }
            SessionState.COMPLETED -> {
                CompletionScreen(
                    finalScore = currentScore,
                    accuracy = 85.5f,
                    onContinue = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun SessionHeader(
    exerciseName: String,
    progress: Float,
    score: Int,
    timeRemaining: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% Tamamlandı",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${timeRemaining}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PreparationScreen(
    exercise: MockExercise,
    countdown: Int,
    onCountdownFinished: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Video Guide Placeholder
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Egzersiz Rehber Videosu",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Countdown
        if (countdown > 0) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$countdown",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hazırlanın...",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActiveExerciseScreen(
    exercise: MockExercise,
    onPauseExercise: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Real-time Feedback Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Green.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Mükemmel! Hareketi doğru yapıyorsunuz.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Green,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Zigzag Tracker
        ZigzagTrackerCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Exercise Video (smaller during active session)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Egzersiz Videosu",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPauseExercise,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Duraklat")
            }
        }
    }
}

@Composable
fun ZigzagTrackerCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "85%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Zigzag Visualization
            ZigzagCanvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Angle Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AngleCard("Mevcut", 45f, MaterialTheme.colorScheme.primary)
                AngleCard("Hedef", 90f, MaterialTheme.colorScheme.secondary)
                AngleCard("Doğruluk", 85f, Color.Green, "%")
            }
        }
    }
}

@Composable
fun ZigzagCanvas(modifier: Modifier = Modifier) {
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
        
        // Draw zigzag pattern
        val path = Path()
        val segmentWidth = width / 20
        val amplitude = height * 0.1f
        
        path.moveTo(-segmentWidth * animatedOffset, centerY)
        
        for (i in 0..21) {
            val x = (i * segmentWidth) - (segmentWidth * animatedOffset)
            if (x > width) break
            if (x < 0) continue
            
            val zigzagOffset = amplitude * sin((i + animatedOffset * 4) * PI / 2).toFloat()
            val y = centerY + zigzagOffset
            path.lineTo(x, y)
        }
        
        drawPath(
            path = path,
            color = Color.Green,
            style = Stroke(
                width = 4.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        )
        
        // Draw target line
        drawLine(
            color = Color.Blue.copy(alpha = 0.5f),
            start = Offset(0f, centerY - amplitude),
            end = Offset(width, centerY - amplitude),
            strokeWidth = 2.dp.toPx()
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

@Composable
fun PausedScreen(
    onResumeExercise: () -> Unit,
    onEndExercise: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Pause,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Egzersiz Duraklatıldı",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onEndExercise
            ) {
                Text("Bitir")
            }
            Button(
                onClick = onResumeExercise
            ) {
                Text("Devam Et")
            }
        }
    }
}

@Composable
fun CompletionScreen(
    finalScore: Int,
    accuracy: Float,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFFFFD700)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Tebrikler!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Egzersizi başarıyla tamamladınız",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Results
        Card {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sonuçlarınız",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$finalScore",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            text = "Puan",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${accuracy.toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Doğruluk",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Devam Et")
        }
    }
}

enum class SessionState {
    PREPARATION, ACTIVE, PAUSED, COMPLETED
}
