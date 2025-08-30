package com.example.physiotherapyapp.presentation.exercise

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.physiotherapyapp.data.model.BodyPart
import com.example.physiotherapyapp.data.model.Exercise
import com.example.physiotherapyapp.data.model.ExerciseDifficulty
import com.example.physiotherapyapp.navigation.Screen

@Composable
fun ExerciseSelectionScreen(
    navController: NavController,
    viewModel: ExerciseSelectionViewModel = hiltViewModel()
) {
    val exercises by viewModel.exercises.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val selectedBodyPart by viewModel.selectedBodyPart.collectAsState()
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    
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
                    text = "Egzersiz Seçimi",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Yapmak istediğiniz egzersizleri seçin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Filters
        FilterSection(
            selectedBodyPart = selectedBodyPart,
            selectedDifficulty = selectedDifficulty,
            onBodyPartSelected = viewModel::selectBodyPart,
            onDifficultySelected = viewModel::selectDifficulty
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Selected Exercises Summary
        if (selectedExercises.isNotEmpty()) {
            SelectedExercisesCard(
                selectedExercises = selectedExercises,
                onRemoveExercise = viewModel::removeExercise,
                onStartSession = {
                    // Navigate to session with selected exercises
                    val exerciseIds = selectedExercises.joinToString(",") { it.id }
                    navController.navigate("exercise_session/$exerciseIds")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Exercise List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(exercises) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    isSelected = selectedExercises.contains(exercise),
                    onToggleSelection = { viewModel.toggleExerciseSelection(exercise) },
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
    selectedBodyPart: BodyPart?,
    selectedDifficulty: ExerciseDifficulty?,
    onBodyPartSelected: (BodyPart?) -> Unit,
    onDifficultySelected: (ExerciseDifficulty?) -> Unit
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
            item {
                FilterChip(
                    onClick = { onBodyPartSelected(null) },
                    label = { Text("Tümü") },
                    selected = selectedBodyPart == null
                )
            }
            items(BodyPart.values()) { bodyPart ->
                FilterChip(
                    onClick = { onBodyPartSelected(bodyPart) },
                    label = { Text(getBodyPartDisplayName(bodyPart)) },
                    selected = selectedBodyPart == bodyPart
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
            item {
                FilterChip(
                    onClick = { onDifficultySelected(null) },
                    label = { Text("Tümü") },
                    selected = selectedDifficulty == null
                )
            }
            items(ExerciseDifficulty.values()) { difficulty ->
                FilterChip(
                    onClick = { onDifficultySelected(difficulty) },
                    label = { Text(getDifficultyDisplayName(difficulty)) },
                    selected = selectedDifficulty == difficulty
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCard(
    exercise: Exercise,
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
                // Exercise Image
                AsyncImage(
                    model = exercise.thumbnailUrl,
                    contentDescription = exercise.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
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
                            icon = Icons.Default.FitnessCenter,
                            text = getDifficultyDisplayName(exercise.difficulty)
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
    selectedExercises: List<Exercise>,
    onRemoveExercise: (Exercise) -> Unit,
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
            
            // Selected exercises list
            selectedExercises.forEach { exercise ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onRemoveExercise(exercise) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kaldır",
                            modifier = Modifier.size(16.dp)
                        )
                    }
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

private fun getBodyPartDisplayName(bodyPart: BodyPart): String {
    return when (bodyPart) {
        BodyPart.ARM -> "Kol"
        BodyPart.LEG -> "Bacak"
        BodyPart.SHOULDER -> "Omuz"
        BodyPart.BACK -> "Sırt"
        BodyPart.NECK -> "Boyun"
        BodyPart.KNEE -> "Diz"
        BodyPart.ANKLE -> "Ayak Bileği"
    }
}

private fun getDifficultyDisplayName(difficulty: ExerciseDifficulty): String {
    return when (difficulty) {
        ExerciseDifficulty.BEGINNER -> "Başlangıç"
        ExerciseDifficulty.INTERMEDIATE -> "Orta"
        ExerciseDifficulty.ADVANCED -> "İleri"
    }
}

