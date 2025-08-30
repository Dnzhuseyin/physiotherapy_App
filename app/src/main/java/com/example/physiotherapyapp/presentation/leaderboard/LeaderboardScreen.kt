package com.example.physiotherapyapp.presentation.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LeaderboardScreen(navController: NavController) {
    var selectedPeriod by remember { mutableStateOf("Aylık") }
    
    val periods = listOf("Haftalık", "Aylık", "Tüm Zamanlar")
    
    val leaderboardData = listOf(
        LeaderboardUser("1", "Ahmet Yılmaz", 2150, 8, 92.5f, 1),
        LeaderboardUser("2", "Ayşe Demir", 1980, 7, 89.3f, 2),
        LeaderboardUser("3", "Mehmet Kaya", 1875, 6, 91.2f, 3),
        LeaderboardUser("current", "Sen", 1250, 5, 85.5f, 4),
        LeaderboardUser("5", "Fatma Özkan", 1180, 5, 88.7f, 5),
        LeaderboardUser("6", "Ali Şahin", 1120, 4, 84.2f, 6),
        LeaderboardUser("7", "Zeynep Acar", 1050, 4, 86.9f, 7),
        LeaderboardUser("8", "Mustafa Çelik", 980, 4, 83.1f, 8),
        LeaderboardUser("9", "Elif Yıldız", 920, 3, 87.4f, 9),
        LeaderboardUser("10", "Osman Koç", 890, 3, 82.8f, 10)
    )
    
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
                                text = "Sıralama Listesi",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Diğer kullanıcılarla yarışın",
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
            // Current User Position
            val currentUser = leaderboardData.find { it.id == "current" }
            currentUser?.let { user ->
                CurrentUserCard(user = user)
            }
        }
        
        item {
            // Top 3 Podium
            if (leaderboardData.size >= 3) {
                PodiumCard(
                    first = leaderboardData[0],
                    second = leaderboardData[1],
                    third = leaderboardData[2]
                )
            }
        }
        
        // Leaderboard List
        itemsIndexed(leaderboardData.drop(3)) { index, user ->
            LeaderboardItem(
                user = user,
                rank = index + 4,
                isCurrentUser = user.id == "current"
            )
        }
    }
}

@Composable
fun CurrentUserCard(user: LeaderboardUser) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Sizin Konumunuz",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "#${user.rank}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${user.points} puan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PodiumCard(
    first: LeaderboardUser,
    second: LeaderboardUser,
    third: LeaderboardUser
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "En İyi 3",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Second Place
                PodiumItem(
                    user = second,
                    rank = 2,
                    height = 80.dp,
                    color = Color(0xFFC0C0C0) // Silver
                )
                
                // First Place
                PodiumItem(
                    user = first,
                    rank = 1,
                    height = 100.dp,
                    color = Color(0xFFFFD700) // Gold
                )
                
                // Third Place
                PodiumItem(
                    user = third,
                    rank = 3,
                    height = 60.dp,
                    color = Color(0xFFCD7F32) // Bronze
                )
            }
        }
    }
}

@Composable
fun PodiumItem(
    user: LeaderboardUser,
    rank: Int,
    height: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.name.first().toString(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = "${user.points}p",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Podium Base
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$rank",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun LeaderboardItem(
    user: LeaderboardUser,
    rank: Int,
    isCurrentUser: Boolean
) {
    Card(
        colors = if (isCurrentUser) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(40.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentUser) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = "Seviye ${user.level}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Points and Stats
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${user.points}p",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Text(
                    text = "${user.accuracy.toInt()}% doğruluk",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class LeaderboardUser(
    val id: String,
    val name: String,
    val points: Int,
    val level: Int,
    val accuracy: Float,
    val rank: Int
)
