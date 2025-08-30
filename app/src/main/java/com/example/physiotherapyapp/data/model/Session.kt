package com.example.physiotherapyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey val id: String,
    val userId: String,
    val exerciseIds: List<String>,
    val startTime: Date,
    val endTime: Date?,
    val totalScore: Int = 0,
    val accuracy: Float = 0f,
    val completedExercises: Int = 0,
    val isCompleted: Boolean = false
)

@Entity(tableName = "exercise_results")
data class ExerciseResult(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val score: Int,
    val accuracy: Float,
    val duration: Int, // actual duration in seconds
    val correctMovements: Int,
    val totalMovements: Int,
    val sensorData: List<SensorReading>
)

data class SensorReading(
    val timestamp: Long,
    val accelerometerX: Float,
    val accelerometerY: Float,
    val accelerometerZ: Float,
    val gyroscopeX: Float,
    val gyroscopeY: Float,
    val gyroscopeZ: Float,
    val angle: Float
)

