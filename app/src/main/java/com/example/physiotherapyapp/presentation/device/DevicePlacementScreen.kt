package com.example.physiotherapyapp.presentation.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.physiotherapyapp.navigation.Screen

@Composable
fun DevicePlacementScreen(navController: NavController) {
    var selectedBodyPart by remember { mutableStateOf<String?>(null) }
    var placementStatus by remember { mutableStateOf("Kontrol Ediliyor") }
    
    val bodyParts = listOf(
        "Kol" to Icons.Default.Person,
        "Bacak" to Icons.Default.DirectionsRun,
        "Omuz" to Icons.Default.Accessibility,
        "Sırt" to Icons.Default.Person,
        "Boyun" to Icons.Default.Face,
        "Diz" to Icons.Default.Accessibility,
        "Ayak Bileği" to Icons.Default.DirectionsWalk
    )
    
    LaunchedEffect(selectedBodyPart) {
        if (selectedBodyPart != null) {
            kotlinx.coroutines.delay(2000)
            placementStatus = "Doğru Yerleştirildi"
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                            text = "Cihaz Yerleştirme",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cihazı doğru bölgeye yerleştirin",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Body Part Selection
        Text(
            text = "Hedef Bölge Seçin",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(bodyParts) { (name, icon) ->
                FilterChip(
                    onClick = { 
                        selectedBodyPart = name
                        placementStatus = "Kontrol Ediliyor"
                    },
                    label = { Text(name) },
                    selected = selectedBodyPart == name,
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = name,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Video Guide Placeholder
        if (selectedBodyPart != null) {
            Text(
                text = "Yerleştirme Rehberi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$selectedBodyPart Yerleştirme Videosu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Placement Status
        PlacementStatusCard(placementStatus)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Continue Button
        Button(
            onClick = { navController.navigate(Screen.ExerciseSelection.route) },
            enabled = placementStatus == "Doğru Yerleştirildi",
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Egzersizlere Geç")
        }
        
        // Instructions
        if (selectedBodyPart != null) {
            Spacer(modifier = Modifier.height(16.dp))
            PlacementInstructions(selectedBodyPart!!)
        }
    }
}

@Composable
fun PlacementStatusCard(status: String) {
    val (statusColor, icon, description) = when (status) {
        "Doğru Yerleştirildi" -> Triple(
            Color.Green,
            Icons.Default.CheckCircle,
            "Cihaz doğru konumda. Egzersizlere başlayabilirsiniz."
        )
        "Yanlış Yerleştirme" -> Triple(
            Color.Red,
            Icons.Default.Error,
            "Cihazı doğru konuma yerleştirin. Video rehberi takip edin."
        )
        else -> Triple(
            Color.Orange,
            Icons.Default.Search,
            "Cihaz konumu kontrol ediliyor..."
        )
    }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PlacementInstructions(bodyPart: String) {
    val instructions = when (bodyPart) {
        "Kol" -> listOf(
            "Cihazı ön kolunuzun üst kısmına yerleştirin",
            "Bandı sıkıca ama rahatsız etmeyecek şekilde bağlayın",
            "Sensörün yukarı baktığından emin olun",
            "Cihazın hareket etmediğini kontrol edin"
        )
        "Bacak" -> listOf(
            "Cihazı uyluk kasınızın ortasına yerleştirin",
            "Bandı güvenli şekilde sabitleyin",
            "Sensörün dışa baktığından emin olun",
            "Yürürken kaymamasını sağlayın"
        )
        else -> listOf(
            "Cihazı belirtilen bölgeye yerleştirin",
            "Güvenli şekilde sabitleyin",
            "Sensör yönünü kontrol edin"
        )
    }
    
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Yerleştirme Talimatları",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            instructions.forEachIndexed { index, instruction ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = instruction,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
