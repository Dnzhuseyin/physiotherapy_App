package com.example.physiotherapyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: Int, // in seconds
    val difficulty: ExerciseDifficulty,
    val bodyPart: BodyPart,
    val instructions: List<String>,
    val targetAngle: Float? = null, // for sensor validation
    val targetMovement: MovementType,
    val points: Int = 10
)

enum class ExerciseDifficulty {
    BEGINNER, INTERMEDIATE, ADVANCED
}

enum class BodyPart {
    ARM, LEG, SHOULDER, BACK, NECK, KNEE, ANKLE
}

enum class MovementType {
    FLEXION, EXTENSION, ROTATION, ABDUCTION, ADDUCTION
}

