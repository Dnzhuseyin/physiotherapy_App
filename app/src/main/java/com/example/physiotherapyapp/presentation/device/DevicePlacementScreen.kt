package com.example.physiotherapyapp.presentation.device

import androidx.compose.foundation.layout.*
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
import com.example.physiotherapyapp.data.bluetooth.DevicePlacement
import com.example.physiotherapyapp.navigation.Screen
import com.example.physiotherapyapp.presentation.components.VideoPlayer

@Composable
fun DevicePlacementScreen(
    navController: NavController,
    viewModel: DevicePlacementViewModel = hiltViewModel()
) {
    val devicePlacement by viewModel.devicePlacement.collectAsState()
    val selectedBodyPart by viewModel.selectedBodyPart.collectAsState()
    val isPlacementCorrect by viewModel.isPlacementCorrect.collectAsState()
    
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
                Text(
                    text = "Cihaz Yerleştirme",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cihazı doğru bölgeye yerleştirin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Body Part Selection
        Text(
            text = "Hedef Bölge Seçin",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        BodyPartSelector(
            selectedBodyPart = selectedBodyPart,
            onBodyPartSelected = viewModel::selectBodyPart
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Video Guide
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
                VideoPlayer(
                    videoUrl = getPlacementVideoUrl(selectedBodyPart!!),
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Placement Status
        PlacementStatusCard(
            placement = devicePlacement,
            isCorrect = isPlacementCorrect
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Continue Button
        Button(
            onClick = { navController.navigate(Screen.ExerciseSelection.route) },
            enabled = isPlacementCorrect,
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
fun BodyPartSelector(
    selectedBodyPart: String?,
    onBodyPartSelected: (String) -> Unit
) {
    val bodyParts = listOf(
        "Kol" to Icons.Default.PanTool,
        "Bacak" to Icons.Default.DirectionsRun,
        "Omuz" to Icons.Default.Accessibility,
        "Sırt" to Icons.Default.Person,
        "Boyun" to Icons.Default.Face,
        "Diz" to Icons.Default.Accessibility,
        "Ayak Bileği" to Icons.Default.DirectionsWalk
    )
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(bodyParts.size) { index ->
            val (name, icon) = bodyParts[index]
            FilterChip(
                onClick = { onBodyPartSelected(name) },
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
}

@Composable
fun PlacementStatusCard(
    placement: DevicePlacement?,
    isCorrect: Boolean
) {
    val (statusText, statusColor, icon, description) = when (placement) {
        DevicePlacement.CORRECT -> {
            Quadruple(
                "Doğru Yerleştirildi",
                Color.Green,
                Icons.Default.CheckCircle,
                "Cihaz doğru konumda. Egzersizlere başlayabilirsiniz."
            )
        }
        DevicePlacement.INCORRECT -> {
            Quadruple(
                "Yanlış Yerleştirme",
                Color.Red,
                Icons.Default.Error,
                "Cihazı doğru konuma yerleştirin. Video rehberi takip edin."
            )
        }
        else -> {
            Quadruple(
                "Kontrol Ediliyor",
                Color.Orange,
                Icons.Default.Sensors,
                "Cihaz konumu kontrol ediliyor..."
            )
        }
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
                        text = statusText,
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
    val instructions = getInstructionsForBodyPart(bodyPart)
    
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

@Composable
private fun LazyRow(
    horizontalArrangement: Arrangement.Horizontal,
    modifier: Modifier,
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier,
        content = content
    )
}

private fun getPlacementVideoUrl(bodyPart: String): String {
    // Return appropriate video URL based on body part
    return when (bodyPart) {
        "Kol" -> "android.resource://com.example.physiotherapyapp/raw/arm_placement"
        "Bacak" -> "android.resource://com.example.physiotherapyapp/raw/leg_placement"
        "Omuz" -> "android.resource://com.example.physiotherapyapp/raw/shoulder_placement"
        else -> "android.resource://com.example.physiotherapyapp/raw/default_placement"
    }
}

private fun getInstructionsForBodyPart(bodyPart: String): List<String> {
    return when (bodyPart) {
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
        "Omuz" -> listOf(
            "Cihazı omuz kasınızın üzerine yerleştirin",
            "Bandı omuz etrafına dolayın",
            "Sensörün yukarı baktığından emin olun",
            "Kol hareketlerinde kaymamasını kontrol edin"
        )
        else -> listOf(
            "Cihazı belirtilen bölgeye yerleştirin",
            "Güvenli şekilde sabitleyin",
            "Sensör yönünü kontrol edin"
        )
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

