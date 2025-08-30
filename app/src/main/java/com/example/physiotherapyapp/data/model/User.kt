package com.example.physiotherapyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val totalPoints: Int = 0,
    val level: Int = 1,
    val totalSessions: Int = 0,
    val totalExercises: Int = 0,
    val averageAccuracy: Float = 0f,
    val achievements: List<String> = emptyList(),
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconUrl: String,
    val pointsRequired: Int,
    val type: AchievementType,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

enum class AchievementType {
    POINTS, SESSIONS, EXERCISES, ACCURACY, STREAK, SPECIAL
}

@Entity(tableName = "user_achievements")
data class UserAchievement(
    @PrimaryKey val id: String,
    val userId: String,
    val achievementId: String,
    val unlockedAt: Long
)

