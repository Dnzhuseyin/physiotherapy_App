package com.example.physiotherapyapp.presentation.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.UserDao
import com.example.physiotherapyapp.data.model.Achievement
import com.example.physiotherapyapp.data.model.AchievementType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()
    
    private val _userProgress = MutableStateFlow(UserProgress(0, 0, 0, 0f, 1))
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()
    
    init {
        loadAchievements()
        loadUserProgress()
    }
    
    private fun loadAchievements() {
        viewModelScope.launch {
            // Initialize sample achievements if not exists
            val sampleAchievements = listOf(
                Achievement(
                    id = "first_session",
                    title = "İlk Adım",
                    description = "İlk egzersiz seansınızı tamamlayın",
                    iconUrl = "",
                    pointsRequired = 1,
                    type = AchievementType.SESSIONS,
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis()
                ),
                Achievement(
                    id = "point_collector",
                    title = "Puan Toplayıcısı",
                    description = "100 puan toplayın",
                    iconUrl = "",
                    pointsRequired = 100,
                    type = AchievementType.POINTS,
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis()
                ),
                Achievement(
                    id = "consistency_master",
                    title = "Tutarlılık Ustası",
                    description = "10 egzersiz seansı tamamlayın",
                    iconUrl = "",
                    pointsRequired = 10,
                    type = AchievementType.SESSIONS,
                    isUnlocked = false
                ),
                Achievement(
                    id = "accuracy_expert",
                    title = "Doğruluk Uzmanı",
                    description = "%90 doğruluk oranına ulaşın",
                    iconUrl = "",
                    pointsRequired = 90,
                    type = AchievementType.ACCURACY,
                    isUnlocked = false
                ),
                Achievement(
                    id = "exercise_enthusiast",
                    title = "Egzersiz Tutkunu",
                    description = "50 farklı egzersiz yapın",
                    iconUrl = "",
                    pointsRequired = 50,
                    type = AchievementType.EXERCISES,
                    isUnlocked = false
                ),
                Achievement(
                    id = "point_master",
                    title = "Puan Ustası",
                    description = "1000 puan toplayın",
                    iconUrl = "",
                    pointsRequired = 1000,
                    type = AchievementType.POINTS,
                    isUnlocked = false
                ),
                Achievement(
                    id = "dedication_champion",
                    title = "Adanmışlık Şampiyonu",
                    description = "30 gün üst üste egzersiz yapın",
                    iconUrl = "",
                    pointsRequired = 30,
                    type = AchievementType.STREAK,
                    isUnlocked = false
                ),
                Achievement(
                    id = "perfectionist",
                    title = "Mükemmeliyetçi",
                    description = "%95 doğruluk oranına ulaşın",
                    iconUrl = "",
                    pointsRequired = 95,
                    type = AchievementType.ACCURACY,
                    isUnlocked = false
                ),
                Achievement(
                    id = "marathon_runner",
                    title = "Maraton Koşucusu",
                    description = "100 egzersiz seansı tamamlayın",
                    iconUrl = "",
                    pointsRequired = 100,
                    type = AchievementType.SESSIONS,
                    isUnlocked = false
                ),
                Achievement(
                    id = "legend",
                    title = "Efsane",
                    description = "Özel başarım: Tüm egzersizleri mükemmel doğrulukla tamamlayın",
                    iconUrl = "",
                    pointsRequired = 100,
                    type = AchievementType.SPECIAL,
                    isUnlocked = false
                )
            )
            
            // Insert achievements to database
            userDao.insertAchievements(sampleAchievements)
            
            // Load achievements from database
            userDao.getAllAchievements().collect { achievementList ->
                _achievements.value = achievementList
            }
        }
    }
    
    private fun loadUserProgress() {
        viewModelScope.launch {
            // Load user data or use sample data
            val user = userDao.getUserById("user1")
            user?.let {
                _userProgress.value = UserProgress(
                    totalPoints = it.totalPoints,
                    totalSessions = it.totalSessions,
                    totalExercises = it.totalExercises,
                    averageAccuracy = it.averageAccuracy,
                    level = it.level
                )
            } ?: run {
                // Sample user progress for demo
                _userProgress.value = UserProgress(
                    totalPoints = 1250,
                    totalSessions = 24,
                    totalExercises = 48,
                    averageAccuracy = 85.5f,
                    level = 5
                )
            }
        }
    }
}

