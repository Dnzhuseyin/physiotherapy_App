package com.example.physiotherapyapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.SessionDao
import com.example.physiotherapyapp.data.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val sessionDao: SessionDao
) : ViewModel() {
    
    private val _user = MutableStateFlow<ProfileUser?>(null)
    val user: StateFlow<ProfileUser?> = _user.asStateFlow()
    
    private val _recentSessions = MutableStateFlow<List<ProfileSession>>(emptyList())
    val recentSessions: StateFlow<List<ProfileSession>> = _recentSessions.asStateFlow()
    
    private val _recentAchievements = MutableStateFlow<List<ProfileAchievement>>(emptyList())
    val recentAchievements: StateFlow<List<ProfileAchievement>> = _recentAchievements.asStateFlow()
    
    init {
        loadUserProfile()
        loadRecentSessions()
        loadRecentAchievements()
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            // Load user data or use sample data
            val userData = userDao.getUserById("user1")
            userData?.let {
                val pointsToNextLevel = calculatePointsToNextLevel(it.level, it.totalPoints)
                val levelProgress = calculateLevelProgress(it.level, it.totalPoints)
                
                _user.value = ProfileUser(
                    name = it.name,
                    level = it.level,
                    totalPoints = it.totalPoints,
                    pointsToNextLevel = pointsToNextLevel,
                    levelProgress = levelProgress,
                    totalSessions = it.totalSessions,
                    totalExercises = it.totalExercises,
                    averageAccuracy = it.averageAccuracy
                )
            } ?: run {
                // Sample user data for demo
                _user.value = ProfileUser(
                    name = "Kullanıcı Adı",
                    level = 5,
                    totalPoints = 1250,
                    pointsToNextLevel = 250,
                    levelProgress = 0.75f,
                    totalSessions = 24,
                    totalExercises = 48,
                    averageAccuracy = 85.5f
                )
            }
        }
    }
    
    private fun loadRecentSessions() {
        viewModelScope.launch {
            // Generate sample recent sessions
            _recentSessions.value = listOf(
                ProfileSession(
                    exerciseName = "Kol Fleksiyonu",
                    date = "2 gün önce",
                    score = 85,
                    accuracy = 88.5f
                ),
                ProfileSession(
                    exerciseName = "Bacak Ekstansiyonu",
                    date = "3 gün önce",
                    score = 92,
                    accuracy = 91.2f
                ),
                ProfileSession(
                    exerciseName = "Omuz Rotasyonu",
                    date = "5 gün önce",
                    score = 78,
                    accuracy = 82.3f
                ),
                ProfileSession(
                    exerciseName = "Diz Fleksiyonu",
                    date = "1 hafta önce",
                    score = 89,
                    accuracy = 86.7f
                )
            )
        }
    }
    
    private fun loadRecentAchievements() {
        viewModelScope.launch {
            // Generate sample recent achievements
            _recentAchievements.value = listOf(
                ProfileAchievement(
                    title = "İlk Adım",
                    description = "İlk egzersiz seansını tamamla"
                ),
                ProfileAchievement(
                    title = "Puan Toplayıcısı",
                    description = "100 puan topla"
                ),
                ProfileAchievement(
                    title = "Tutarlılık",
                    description = "5 gün üst üste egzersiz yap"
                )
            )
        }
    }
    
    private fun calculatePointsToNextLevel(currentLevel: Int, currentPoints: Int): Int {
        val pointsForNextLevel = currentLevel * 300 // Example: each level requires 300 more points
        val pointsInCurrentLevel = currentPoints % 300
        return pointsForNextLevel - pointsInCurrentLevel
    }
    
    private fun calculateLevelProgress(currentLevel: Int, currentPoints: Int): Float {
        val pointsInCurrentLevel = currentPoints % 300
        return pointsInCurrentLevel / 300f
    }
}

