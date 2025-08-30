package com.example.physiotherapyapp.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.ExerciseDao
import com.example.physiotherapyapp.data.model.BodyPart
import com.example.physiotherapyapp.data.model.Exercise
import com.example.physiotherapyapp.data.model.ExerciseDifficulty
import com.example.physiotherapyapp.data.model.MovementType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSelectionViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ViewModel() {
    
    private val _selectedExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val selectedExercises: StateFlow<List<Exercise>> = _selectedExercises.asStateFlow()
    
    private val _selectedBodyPart = MutableStateFlow<BodyPart?>(null)
    val selectedBodyPart: StateFlow<BodyPart?> = _selectedBodyPart.asStateFlow()
    
    private val _selectedDifficulty = MutableStateFlow<ExerciseDifficulty?>(null)
    val selectedDifficulty: StateFlow<ExerciseDifficulty?> = _selectedDifficulty.asStateFlow()
    
    val exercises = combine(
        exerciseDao.getAllExercises(),
        _selectedBodyPart,
        _selectedDifficulty
    ) { allExercises, bodyPart, difficulty ->
        var filteredExercises = allExercises
        
        if (bodyPart != null) {
            filteredExercises = filteredExercises.filter { it.bodyPart == bodyPart }
        }
        
        if (difficulty != null) {
            filteredExercises = filteredExercises.filter { it.difficulty == difficulty }
        }
        
        filteredExercises
    }
    
    init {
        // Initialize with sample exercises
        initializeSampleExercises()
    }
    
    fun toggleExerciseSelection(exercise: Exercise) {
        val currentSelected = _selectedExercises.value.toMutableList()
        if (currentSelected.contains(exercise)) {
            currentSelected.remove(exercise)
        } else {
            currentSelected.add(exercise)
        }
        _selectedExercises.value = currentSelected
    }
    
    fun removeExercise(exercise: Exercise) {
        val currentSelected = _selectedExercises.value.toMutableList()
        currentSelected.remove(exercise)
        _selectedExercises.value = currentSelected
    }
    
    fun selectBodyPart(bodyPart: BodyPart?) {
        _selectedBodyPart.value = bodyPart
    }
    
    fun selectDifficulty(difficulty: ExerciseDifficulty?) {
        _selectedDifficulty.value = difficulty
    }
    
    private fun initializeSampleExercises() {
        viewModelScope.launch {
            val sampleExercises = listOf(
                Exercise(
                    id = "1",
                    name = "Kol Fleksiyonu",
                    description = "Kolunuzu yukarı kaldırın ve indirin",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/arm_flexion",
                    thumbnailUrl = "https://example.com/thumb1.jpg",
                    duration = 120,
                    difficulty = ExerciseDifficulty.BEGINNER,
                    bodyPart = BodyPart.ARM,
                    instructions = listOf(
                        "Kolunuzu yan tarafınızda tutun",
                        "Yavaşça yukarı kaldırın",
                        "90 dereceye geldiğinde durdurun",
                        "Yavaşça başlangıç pozisyonuna dönün"
                    ),
                    targetAngle = 90f,
                    targetMovement = MovementType.FLEXION,
                    points = 15
                ),
                Exercise(
                    id = "2",
                    name = "Bacak Ekstansiyonu",
                    description = "Bacağınızı düz şekilde uzatın",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/leg_extension",
                    thumbnailUrl = "https://example.com/thumb2.jpg",
                    duration = 180,
                    difficulty = ExerciseDifficulty.INTERMEDIATE,
                    bodyPart = BodyPart.LEG,
                    instructions = listOf(
                        "Sandalyeye oturun",
                        "Bacağınızı yavaşça uzatın",
                        "Tam uzatma pozisyonunda 2 saniye bekleyin",
                        "Yavaşça indirin"
                    ),
                    targetAngle = 0f,
                    targetMovement = MovementType.EXTENSION,
                    points = 20
                ),
                Exercise(
                    id = "3",
                    name = "Omuz Rotasyonu",
                    description = "Omzunuzu dairesel hareketle çevirin",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/shoulder_rotation",
                    thumbnailUrl = "https://example.com/thumb3.jpg",
                    duration = 90,
                    difficulty = ExerciseDifficulty.BEGINNER,
                    bodyPart = BodyPart.SHOULDER,
                    instructions = listOf(
                        "Ayakta dik durun",
                        "Kollarınızı yanınızda tutun",
                        "Omuzlarınızı yavaşça geriye doğru çevirin",
                        "10 tekrar yapın"
                    ),
                    targetAngle = 180f,
                    targetMovement = MovementType.ROTATION,
                    points = 12
                ),
                Exercise(
                    id = "4",
                    name = "Diz Fleksiyonu",
                    description = "Dizinizi bükerek esneklik kazanın",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/knee_flexion",
                    thumbnailUrl = "https://example.com/thumb4.jpg",
                    duration = 150,
                    difficulty = ExerciseDifficulty.INTERMEDIATE,
                    bodyPart = BodyPart.KNEE,
                    instructions = listOf(
                        "Yüzüstü yatın",
                        "Dizinizi yavaşça bükerek topuğunuzu kalçanıza yaklaştırın",
                        "Maximum büküm noktasında 3 saniye bekleyin",
                        "Yavaşça başlangıç pozisyonuna dönün"
                    ),
                    targetAngle = 120f,
                    targetMovement = MovementType.FLEXION,
                    points = 18
                ),
                Exercise(
                    id = "5",
                    name = "Ayak Bileği Rotasyonu",
                    description = "Ayak bileğinizi dairesel hareketle çevirin",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/ankle_rotation",
                    thumbnailUrl = "https://example.com/thumb5.jpg",
                    duration = 60,
                    difficulty = ExerciseDifficulty.BEGINNER,
                    bodyPart = BodyPart.ANKLE,
                    instructions = listOf(
                        "Oturarak bacağınızı uzatın",
                        "Ayak bileğinizi saat yönünde çevirin",
                        "10 tekrar saat yönünde",
                        "10 tekrar saat yönünün tersinde"
                    ),
                    targetAngle = 360f,
                    targetMovement = MovementType.ROTATION,
                    points = 10
                ),
                Exercise(
                    id = "6",
                    name = "İleri Seviye Kol Egzersizi",
                    description = "Karmaşık kol hareketleri kombinasyonu",
                    videoUrl = "android.resource://com.example.physiotherapyapp/raw/advanced_arm",
                    thumbnailUrl = "https://example.com/thumb6.jpg",
                    duration = 240,
                    difficulty = ExerciseDifficulty.ADVANCED,
                    bodyPart = BodyPart.ARM,
                    instructions = listOf(
                        "Bu egzersiz deneyimli kullanıcılar içindir",
                        "Kolunuzu farklı açılarda hareket ettirin",
                        "Her pozisyonda 5 saniye bekleyin",
                        "Yavaş ve kontrollü hareket edin"
                    ),
                    targetAngle = 135f,
                    targetMovement = MovementType.ABDUCTION,
                    points = 30
                )
            )
            
            exerciseDao.insertExercises(sampleExercises)
        }
    }
}

