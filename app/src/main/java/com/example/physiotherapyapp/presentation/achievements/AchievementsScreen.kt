package com.example.physiotherapyapp.presentation.achievements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AchievementsScreen(
    navController: NavController,
    viewModel: AchievementsViewModel = hiltViewModel()
) {
    val achievements by viewModel.achievements.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    
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
                    Text(
                        text = "Başarımlar",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rozetlerinizi kazanın ve ilerlemenizi takip edin",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            // Progress Summary
            ProgressSummaryCard(
                unlockedCount = achievements.count { it.isUnlocked },
                totalCount = achievements.size,
                currentPoints = userProgress.totalPoints,
                currentLevel = userProgress.level
            )
        }
        
        items(achievements) { achievement ->
            AchievementCard(
                achievement = achievement,
                userProgress = userProgress
            )
        }
    }
}

@Composable
fun ProgressSummaryCard(
    unlockedCount: Int,
    totalCount: Int,
    currentPoints: Int,
    currentLevel: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "İlerleme Özeti",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$unlockedCount/$totalCount rozet kazanıldı",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Seviye $currentLevel",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "$currentPoints puan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress bar
            val progress = unlockedCount.toFloat() / totalCount
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${(progress * 100).toInt()}% tamamlandı",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AchievementCard(
    achievement: com.example.physiotherapyapp.data.model.Achievement,
    userProgress: UserProgress
) {
    val isUnlocked = achievement.isUnlocked
    val progress = when (achievement.type) {
        com.example.physiotherapyapp.data.model.AchievementType.POINTS -> 
            (userProgress.totalPoints.toFloat() / achievement.pointsRequired).coerceAtMost(1f)
        com.example.physiotherapyapp.data.model.AchievementType.SESSIONS -> 
            (userProgress.totalSessions.toFloat() / achievement.pointsRequired).coerceAtMost(1f)
        com.example.physiotherapyapp.data.model.AchievementType.EXERCISES -> 
            (userProgress.totalExercises.toFloat() / achievement.pointsRequired).coerceAtMost(1f)
        com.example.physiotherapyapp.data.model.AchievementType.ACCURACY -> 
            (userProgress.averageAccuracy / achievement.pointsRequired).coerceAtMost(1f)
        else -> if (isUnlocked) 1f else 0f
    }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) 
                Color(0xFFFFD700).copy(alpha = 0.1f) 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Achievement Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getAchievementIcon(achievement.type),
                    contentDescription = null,
                    tint = if (isUnlocked) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(36.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Achievement Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (!isUnlocked) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Progress bar for locked achievements
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = getProgressText(achievement, userProgress),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Status indicator
            if (isUnlocked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Kazanıldı",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

private fun getAchievementIcon(type: com.example.physiotherapyapp.data.model.AchievementType): androidx.compose.ui.graphics.vector.ImageVector {
    return when (type) {
        com.example.physiotherapyapp.data.model.AchievementType.POINTS -> Icons.Default.EmojiEvents
        com.example.physiotherapyapp.data.model.AchievementType.SESSIONS -> Icons.Default.FitnessCenter
        com.example.physiotherapyapp.data.model.AchievementType.EXERCISES -> Icons.Default.DirectionsRun
        com.example.physiotherapyapp.data.model.AchievementType.ACCURACY -> Icons.Default.TrendingUp
        com.example.physiotherapyapp.data.model.AchievementType.STREAK -> Icons.Default.Whatshot
        com.example.physiotherapyapp.data.model.AchievementType.SPECIAL -> Icons.Default.Star
    }
}

private fun getProgressText(
    achievement: com.example.physiotherapyapp.data.model.Achievement,
    userProgress: UserProgress
): String {
    return when (achievement.type) {
        com.example.physiotherapyapp.data.model.AchievementType.POINTS -> 
            "${userProgress.totalPoints}/${achievement.pointsRequired} puan"
        com.example.physiotherapyapp.data.model.AchievementType.SESSIONS -> 
            "${userProgress.totalSessions}/${achievement.pointsRequired} seans"
        com.example.physiotherapyapp.data.model.AchievementType.EXERCISES -> 
            "${userProgress.totalExercises}/${achievement.pointsRequired} egzersiz"
        com.example.physiotherapyapp.data.model.AchievementType.ACCURACY -> 
            "${userProgress.averageAccuracy.toInt()}%/${achievement.pointsRequired}% doğruluk"
        else -> "İlerleme takip ediliyor"
    }
}

data class UserProgress(
    val totalPoints: Int,
    val totalSessions: Int,
    val totalExercises: Int,
    val averageAccuracy: Float,
    val level: Int
)

