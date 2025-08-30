package com.example.physiotherapyapp.presentation.exercise

import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.physiotherapyapp.presentation.components.VideoPlayer
import com.example.physiotherapyapp.presentation.components.ZigzagTracker
import kotlinx.coroutines.delay

@Composable
fun ExerciseSessionScreen(
    exerciseId: String,
    navController: NavController,
    viewModel: ExerciseSessionViewModel = hiltViewModel()
) {
    val currentExercise by viewModel.currentExercise.collectAsState()
    val sensorData by viewModel.sensorData.collectAsState()
    val sessionState by viewModel.sessionState.collectAsState()
    val currentScore by viewModel.currentScore.collectAsState()
    val exerciseProgress by viewModel.exerciseProgress.collectAsState()
    val feedback by viewModel.feedback.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    
    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }
    
    currentExercise?.let { exercise ->
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
                        onStartExercise = viewModel::startExercise
                    )
                }
                SessionState.ACTIVE -> {
                    ActiveExerciseScreen(
                        exercise = exercise,
                        sensorData = sensorData,
                        feedback = feedback,
                        onPauseExercise = viewModel::pauseExercise
                    )
                }
                SessionState.PAUSED -> {
                    PausedScreen(
                        onResumeExercise = viewModel::resumeExercise,
                        onEndExercise = viewModel::endExercise
                    )
                }
                SessionState.COMPLETED -> {
                    CompletionScreen(
                        finalScore = currentScore,
                        accuracy = viewModel.getFinalAccuracy(),
                        onContinue = { navController.popBackStack() }
                    )
                }
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
    exercise: com.example.physiotherapyapp.data.model.Exercise,
    onStartExercise: () -> Unit
) {
    var countdown by remember { mutableStateOf(5) }
    
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        } else {
            onStartExercise()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Video Guide
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            VideoPlayer(
                videoUrl = exercise.videoUrl,
                modifier = Modifier.fillMaxSize(),
                autoPlay = true
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Instructions
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Egzersiz Talimatları",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                exercise.instructions.forEachIndexed { index, instruction ->
                    Text(
                        text = "${index + 1}. $instruction",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
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
    exercise: com.example.physiotherapyapp.data.model.Exercise,
    sensorData: com.example.physiotherapyapp.data.model.SensorReading?,
    feedback: ExerciseFeedback?,
    onPauseExercise: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Real-time Feedback
        feedback?.let { 
            FeedbackCard(feedback = it)
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        // Zigzag Tracker
        ZigzagTracker(
            sensorData = sensorData,
            targetAngle = exercise.targetAngle ?: 0f,
            isCorrect = feedback?.isCorrect == true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Exercise Video (smaller during active session)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            VideoPlayer(
                videoUrl = exercise.videoUrl,
                modifier = Modifier.fillMaxSize(),
                autoPlay = true
            )
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
fun FeedbackCard(feedback: ExerciseFeedback) {
    val backgroundColor = when (feedback.type) {
        FeedbackType.CORRECT -> Color.Green.copy(alpha = 0.1f)
        FeedbackType.INCORRECT -> Color.Red.copy(alpha = 0.1f)
        FeedbackType.GUIDANCE -> Color.Orange.copy(alpha = 0.1f)
    }
    
    val textColor = when (feedback.type) {
        FeedbackType.CORRECT -> Color.Green
        FeedbackType.INCORRECT -> Color.Red
        FeedbackType.GUIDANCE -> Color.Orange
    }
    
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (feedback.type) {
                    FeedbackType.CORRECT -> Icons.Default.CheckCircle
                    FeedbackType.INCORRECT -> Icons.Default.Error
                    FeedbackType.GUIDANCE -> Icons.Default.Info
                },
                contentDescription = null,
                tint = textColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = feedback.message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
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

data class ExerciseFeedback(
    val message: String,
    val type: FeedbackType,
    val isCorrect: Boolean
)

enum class FeedbackType {
    CORRECT, INCORRECT, GUIDANCE
}

