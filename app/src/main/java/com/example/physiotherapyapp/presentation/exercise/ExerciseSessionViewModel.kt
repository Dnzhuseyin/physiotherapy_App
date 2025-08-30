package com.example.physiotherapyapp.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.bluetooth.PhysiotherapyBluetoothManager
import com.example.physiotherapyapp.data.database.dao.ExerciseDao
import com.example.physiotherapyapp.data.database.dao.SessionDao
import com.example.physiotherapyapp.data.model.Exercise
import com.example.physiotherapyapp.data.model.ExerciseResult
import com.example.physiotherapyapp.data.model.SensorReading
import com.example.physiotherapyapp.data.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ExerciseSessionViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val sessionDao: SessionDao,
    private val bluetoothManager: PhysiotherapyBluetoothManager
) : ViewModel() {
    
    private val _currentExercise = MutableStateFlow<Exercise?>(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise.asStateFlow()
    
    private val _sessionState = MutableStateFlow(SessionState.PREPARATION)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()
    
    private val _currentScore = MutableStateFlow(0)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()
    
    private val _exerciseProgress = MutableStateFlow(0f)
    val exerciseProgress: StateFlow<Float> = _exerciseProgress.asStateFlow()
    
    private val _feedback = MutableStateFlow<ExerciseFeedback?>(null)
    val feedback: StateFlow<ExerciseFeedback?> = _feedback.asStateFlow()
    
    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()
    
    val sensorData: StateFlow<SensorReading?> = bluetoothManager.sensorData
    
    private var sessionId: String = ""
    private var exerciseStartTime: Long = 0
    private var exerciseTimer: Job? = null
    private var correctMovements = 0
    private var totalMovements = 0
    private val sensorReadings = mutableListOf<SensorReading>()
    private var accuracySum = 0f
    private var accuracyCount = 0
    
    fun loadExercise(exerciseId: String) {
        viewModelScope.launch {
            val exercise = exerciseDao.getExerciseById(exerciseId)
            _currentExercise.value = exercise
            exercise?.let {
                _timeRemaining.value = it.duration
                sessionId = UUID.randomUUID().toString()
            }
        }
    }
    
    fun startExercise() {
        val exercise = _currentExercise.value ?: return
        
        _sessionState.value = SessionState.ACTIVE
        exerciseStartTime = System.currentTimeMillis()
        
        // Start monitoring sensor data
        startSensorMonitoring()
        
        // Start exercise timer
        startExerciseTimer(exercise.duration)
        
        // Create session record
        viewModelScope.launch {
            val session = Session(
                id = sessionId,
                userId = "user1", // This would come from user authentication
                exerciseIds = listOf(exercise.id),
                startTime = Date(exerciseStartTime),
                endTime = null
            )
            sessionDao.insertSession(session)
        }
    }
    
    fun pauseExercise() {
        _sessionState.value = SessionState.PAUSED
        exerciseTimer?.cancel()
    }
    
    fun resumeExercise() {
        _sessionState.value = SessionState.ACTIVE
        val exercise = _currentExercise.value ?: return
        startExerciseTimer(_timeRemaining.value)
    }
    
    fun endExercise() {
        _sessionState.value = SessionState.COMPLETED
        exerciseTimer?.cancel()
        
        // Calculate final results
        val finalAccuracy = if (accuracyCount > 0) accuracySum / accuracyCount else 0f
        val exercisePoints = _currentExercise.value?.points ?: 0
        val bonusPoints = (finalAccuracy / 10).toInt() // Bonus based on accuracy
        val finalScore = exercisePoints + bonusPoints
        
        _currentScore.value = finalScore
        
        // Save exercise result
        saveExerciseResult(finalAccuracy)
    }
    
    private fun startSensorMonitoring() {
        viewModelScope.launch {
            sensorData.collect { reading ->
                reading?.let { 
                    processSensorReading(it)
                    sensorReadings.add(it)
                }
            }
        }
    }
    
    private fun processSensorReading(reading: SensorReading) {
        val exercise = _currentExercise.value ?: return
        val targetAngle = exercise.targetAngle ?: 0f
        
        totalMovements++
        
        val accuracy = calculateAccuracy(reading.angle, targetAngle)
        accuracySum += accuracy
        accuracyCount++
        
        val isCorrect = accuracy > 80f
        
        if (isCorrect) {
            correctMovements++
            _currentScore.value += 1
        }
        
        // Provide real-time feedback
        val feedback = when {
            isCorrect -> ExerciseFeedback(
                message = "Mükemmel! Hareketi doğru yapıyorsunuz.",
                type = FeedbackType.CORRECT,
                isCorrect = true
            )
            accuracy > 60f -> ExerciseFeedback(
                message = "İyi! Biraz daha hassas olun.",
                type = FeedbackType.GUIDANCE,
                isCorrect = false
            )
            else -> ExerciseFeedback(
                message = "Hareket açısını kontrol edin. Video rehberi takip edin.",
                type = FeedbackType.INCORRECT,
                isCorrect = false
            )
        }
        
        _feedback.value = feedback
        
        // Clear feedback after 3 seconds
        viewModelScope.launch {
            delay(3000)
            if (_feedback.value == feedback) {
                _feedback.value = null
            }
        }
    }
    
    private fun startExerciseTimer(durationSeconds: Int) {
        exerciseTimer = viewModelScope.launch {
            var remaining = durationSeconds
            
            while (remaining > 0 && _sessionState.value == SessionState.ACTIVE) {
                _timeRemaining.value = remaining
                val totalDuration = _currentExercise.value?.duration ?: durationSeconds
                _exerciseProgress.value = (totalDuration - remaining).toFloat() / totalDuration
                
                delay(1000)
                remaining--
            }
            
            if (remaining <= 0) {
                endExercise()
            }
        }
    }
    
    private fun saveExerciseResult(finalAccuracy: Float) {
        viewModelScope.launch {
            val exercise = _currentExercise.value ?: return@launch
            
            val result = ExerciseResult(
                id = UUID.randomUUID().toString(),
                sessionId = sessionId,
                exerciseId = exercise.id,
                score = _currentScore.value,
                accuracy = finalAccuracy,
                duration = exercise.duration - _timeRemaining.value,
                correctMovements = correctMovements,
                totalMovements = totalMovements,
                sensorData = sensorReadings.toList()
            )
            
            sessionDao.insertExerciseResult(result)
            
            // Update session
            val session = sessionDao.getSessionById(sessionId)
            session?.let {
                val updatedSession = it.copy(
                    endTime = Date(),
                    totalScore = _currentScore.value,
                    accuracy = finalAccuracy,
                    completedExercises = 1,
                    isCompleted = true
                )
                sessionDao.updateSession(updatedSession)
            }
        }
    }
    
    private fun calculateAccuracy(currentAngle: Float, targetAngle: Float): Float {
        val difference = abs(currentAngle - targetAngle)
        val maxDifference = 90f
        return ((maxDifference - difference) / maxDifference * 100f).coerceIn(0f, 100f)
    }
    
    fun getFinalAccuracy(): Float {
        return if (accuracyCount > 0) accuracySum / accuracyCount else 0f
    }
    
    override fun onCleared() {
        super.onCleared()
        exerciseTimer?.cancel()
    }
}

