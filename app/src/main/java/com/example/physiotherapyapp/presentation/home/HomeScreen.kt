package com.example.physiotherapyapp.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.physiotherapyapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val menuItems = listOf(
        HomeMenuItem(
            icon = Icons.Default.Bluetooth,
            title = "Cihaz Bağlantısı",
            subtitle = "Fizik tedavi cihazınızı bağlayın",
            route = Screen.DeviceConnection.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
            )
        ),
        HomeMenuItem(
            icon = Icons.Default.FitnessCenter,
            title = "Egzersizler",
            subtitle = "Egzersizleri seçin ve başlayın",
            route = Screen.ExerciseSelection.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF11998e), Color(0xFF38ef7d))
            )
        ),
        HomeMenuItem(
            icon = Icons.Default.Analytics,
            title = "İstatistikler",
            subtitle = "İlerlemenizi takip edin",
            route = Screen.Statistics.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFf093fb), Color(0xFFf5576c))
            )
        ),
        HomeMenuItem(
            icon = Icons.Default.EmojiEvents,
            title = "Başarımlar",
            subtitle = "Rozetlerinizi görüntüleyin",
            route = Screen.Achievements.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
            )
        ),
        HomeMenuItem(
            icon = Icons.Default.Leaderboard,
            title = "Sıralama",
            subtitle = "Diğer kullanıcılarla yarışın",
            route = Screen.Leaderboard.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF43e97b), Color(0xFF38f9d7))
            )
        ),
        HomeMenuItem(
            icon = Icons.Default.Person,
            title = "Profil",
            subtitle = "Hesap bilgileriniz",
            route = Screen.Profile.route,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFfa709a), Color(0xFFfee140))
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Fizik Tedavi Uygulaması",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sağlıklı yaşam için egzersizlerinizi takip edin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        // Menu Items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuItems) { item ->
                HomeMenuCard(
                    item = item,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenuCard(
    item: HomeMenuItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(item.gradient)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Git",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

data class HomeMenuItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val route: String,
    val gradient: Brush
)

