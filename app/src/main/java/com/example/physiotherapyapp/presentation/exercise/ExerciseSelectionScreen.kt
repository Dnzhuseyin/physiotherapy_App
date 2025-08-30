package com.example.physiotherapyapp.presentation.exercise

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.physiotherapyapp.navigation.Screen

@Composable
fun ExerciseSelectionScreen(navController: NavController) {
    var selectedBodyPart by remember { mutableStateOf<String?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedExercises by remember { mutableStateOf(setOf<MockExercise>()) }
    
    val bodyParts = listOf("Tümü", "Kol", "Bacak", "Omuz", "Sırt", "Boyun", "Diz")
    val difficulties = listOf("Tümü", "Başlangıç", "Orta", "İleri")
    
    val mockExercises = listOf(
        MockExercise("1", "Kol Fleksiyonu", "Kolunuzu yukarı kaldırın ve indirin", 120, "Başlangıç", "Kol", 15),
        MockExercise("2", "Bacak Ekstansiyonu", "Bacağınızı düz şekilde uzatın", 180, "Orta", "Bacak", 20),
        MockExercise("3", "Omuz Rotasyonu", "Omzunuzu dairesel hareketle çevirin", 90, "Başlangıç", "Omuz", 12),
        MockExercise("4", "Diz Fleksiyonu", "Dizinizi bükerek esneklik kazanın", 150, "Orta", "Diz", 18),
        MockExercise("5", "Ayak Bileği Rotasyonu", "Ayak bileğinizi dairesel hareketle çevirin", 60, "Başlangıç", "Bacak", 10),
        MockExercise("6", "İleri Seviye Kol Egzersizi", "Karmaşık kol hareketleri kombinasyonu", 240, "İleri", "Kol", 30)
    )
    
    val filteredExercises = mockExercises.filter { exercise ->
        (selectedBodyPart == null || selectedBodyPart == "Tümü" || exercise.bodyPart == selectedBodyPart) &&
        (selectedDifficulty == null || selectedDifficulty == "Tümü" || exercise.difficulty == selectedDifficulty)
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
                            text = "Egzersiz Seçimi",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Yapmak istediğiniz egzersizleri seçin",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Filters
        FilterSection(
            bodyParts = bodyParts,
            difficulties = difficulties,
            selectedBodyPart = selectedBodyPart,
            selectedDifficulty = selectedDifficulty,
            onBodyPartSelected = { selectedBodyPart = it },
            onDifficultySelected = { selectedDifficulty = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selected Exercises Summary
        if (selectedExercises.isNotEmpty()) {
            SelectedExercisesCard(
                selectedExercises = selectedExercises.toList(),
                onRemoveExercise = { exercise ->
                    selectedExercises = selectedExercises - exercise
                },
                onStartSession = {
                    navController.navigate(Screen.ExerciseSession.createRoute("selected"))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Exercise List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredExercises) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    isSelected = selectedExercises.contains(exercise),
                    onToggleSelection = { 
                        selectedExercises = if (selectedExercises.contains(exercise)) {
                            selectedExercises - exercise
                        } else {
                            selectedExercises + exercise
                        }
                    },
                    onPreview = {
                        navController.navigate(Screen.ExerciseVideo.createRoute(exercise.id))
                    }
                )
            }
        }
    }
}

@Composable
fun FilterSection(
    bodyParts: List<String>,
    difficulties: List<String>,
    selectedBodyPart: String?,
    selectedDifficulty: String?,
    onBodyPartSelected: (String?) -> Unit,
    onDifficultySelected: (String?) -> Unit
) {
    Column {
        Text(
            text = "Filtreler",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Body Part Filter
        Text(
            text = "Vücut Bölgesi",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(bodyParts) { bodyPart ->
                FilterChip(
                    onClick = { onBodyPartSelected(if (bodyPart == "Tümü") null else bodyPart) },
                    label = { Text(bodyPart) },
                    selected = (bodyPart == "Tümü" && selectedBodyPart == null) || selectedBodyPart == bodyPart
                )
            }
        }
        
        // Difficulty Filter
        Text(
            text = "Zorluk Seviyesi",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(difficulties) { difficulty ->
                FilterChip(
                    onClick = { onDifficultySelected(if (difficulty == "Tümü") null else difficulty) },
                    label = { Text(difficulty) },
                    selected = (difficulty == "Tümü" && selectedDifficulty == null) || selectedDifficulty == difficulty
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: MockExercise,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    onPreview: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Exercise Image Placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Exercise Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Exercise Details
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ExerciseDetailChip(
                            icon = Icons.Default.Timer,
                            text = "${exercise.duration / 60}dk"
                        )
                        ExerciseDetailChip(
                            icon = Icons.Default.Star,
                            text = exercise.difficulty
                        )
                        ExerciseDetailChip(
                            icon = Icons.Default.EmojiEvents,
                            text = "${exercise.points}p"
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Önizle")
                }
                
                Button(
                    onClick = onToggleSelection,
                    modifier = Modifier.weight(1f),
                    colors = if (isSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.Remove else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isSelected) "Çıkar" else "Ekle")
                }
            }
        }
    }
}

@Composable
fun ExerciseDetailChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SelectedExercisesCard(
    selectedExercises: List<MockExercise>,
    onRemoveExercise: (MockExercise) -> Unit,
    onStartSession: () -> Unit
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
                Text(
                    text = "Seçilen Egzersizler (${selectedExercises.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val totalDuration = selectedExercises.sumOf { it.duration }
                val totalPoints = selectedExercises.sumOf { it.points }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${totalDuration / 60} dk",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "$totalPoints puan",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onStartSession,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seansı Başlat")
            }
        }
    }
}

data class MockExercise(
    val id: String,
    val name: String,
    val description: String,
    val duration: Int,
    val difficulty: String,
    val bodyPart: String,
    val points: Int
)
