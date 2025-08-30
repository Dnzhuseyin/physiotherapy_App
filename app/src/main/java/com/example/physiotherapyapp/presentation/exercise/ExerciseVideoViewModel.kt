package com.example.physiotherapyapp.presentation.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.ExerciseDao
import com.example.physiotherapyapp.data.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseVideoViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ViewModel() {
    
    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise.asStateFlow()
    
    fun loadExercise(exerciseId: String) {
        viewModelScope.launch {
            val exercise = exerciseDao.getExerciseById(exerciseId)
            _exercise.value = exercise
        }
    }
}

