package com.example.physiotherapyapp.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.SessionDao
import com.example.physiotherapyapp.data.database.dao.UserDao
import com.example.physiotherapyapp.presentation.components.BarChartData
import com.example.physiotherapyapp.presentation.components.ChartData
import com.example.physiotherapyapp.presentation.components.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val sessionDao: SessionDao,
    private val userDao: UserDao
) : ViewModel() {
    
    private val _statistics = MutableStateFlow(UserStatistics())
    val statistics: StateFlow<UserStatistics> = _statistics.asStateFlow()
    
    private val _selectedPeriod = MutableStateFlow(StatisticsPeriod.MONTH)
    val selectedPeriod: StateFlow<StatisticsPeriod> = _selectedPeriod.asStateFlow()
    
    private val _progressData = MutableStateFlow<List<ChartData>>(emptyList())
    val progressData: StateFlow<List<ChartData>> = _progressData.asStateFlow()
    
    private val _bodyPartData = MutableStateFlow<List<PieChartData>>(emptyList())
    val bodyPartData: StateFlow<List<PieChartData>> = _bodyPartData.asStateFlow()
    
    private val _accuracyData = MutableStateFlow<List<BarChartData>>(emptyList())
    val accuracyData: StateFlow<List<BarChartData>> = _accuracyData.asStateFlow()
    
    init {
        loadStatistics()
        loadChartData()
    }
    
    fun selectPeriod(period: StatisticsPeriod) {
        _selectedPeriod.value = period
        loadChartData()
    }
    
    private fun loadStatistics() {
        viewModelScope.launch {
            // Load user statistics
            val user = userDao.getUserById("user1") // This would come from authentication
            user?.let {
                _statistics.value = UserStatistics(
                    totalSessions = it.totalSessions,
                    totalPoints = it.totalPoints,
                    averageAccuracy = it.averageAccuracy,
                    level = it.level,
                    totalExercises = it.totalExercises
                )
            } ?: run {
                // Generate sample statistics for demo
                _statistics.value = UserStatistics(
                    totalSessions = 24,
                    totalPoints = 1250,
                    averageAccuracy = 85.5f,
                    level = 5,
                    totalExercises = 48
                )
            }
        }
    }
    
    private fun loadChartData() {
        viewModelScope.launch {
            // Load progress data
            _progressData.value = generateSampleProgressData()
            
            // Load body part distribution
            _bodyPartData.value = listOf(
                PieChartData("Kol", 30f, Color(0xFF4CAF50)),
                PieChartData("Bacak", 25f, Color(0xFF2196F3)),
                PieChartData("Omuz", 20f, Color(0xFFFF9800)),
                PieChartData("Sırt", 15f, Color(0xFF9C27B0)),
                PieChartData("Diz", 10f, Color(0xFFF44336))
            )
            
            // Load accuracy data
            _accuracyData.value = listOf(
                BarChartData("Kol", 88f, Color(0xFF4CAF50)),
                BarChartData("Bacak", 82f, Color(0xFF2196F3)),
                BarChartData("Omuz", 91f, Color(0xFFFF9800)),
                BarChartData("Sırt", 75f, Color(0xFF9C27B0)),
                BarChartData("Diz", 85f, Color(0xFFF44336))
            )
        }
    }
    
    private fun generateSampleProgressData(): List<ChartData> {
        return when (_selectedPeriod.value) {
            StatisticsPeriod.WEEK -> listOf(
                ChartData("Pzt", 75f),
                ChartData("Sal", 82f),
                ChartData("Çar", 88f),
                ChartData("Per", 85f),
                ChartData("Cum", 91f),
                ChartData("Cmt", 87f),
                ChartData("Paz", 89f)
            )
            StatisticsPeriod.MONTH -> listOf(
                ChartData("1. Hafta", 78f),
                ChartData("2. Hafta", 82f),
                ChartData("3. Hafta", 86f),
                ChartData("4. Hafta", 89f)
            )
            StatisticsPeriod.THREE_MONTHS -> listOf(
                ChartData("1. Ay", 75f),
                ChartData("2. Ay", 82f),
                ChartData("3. Ay", 89f)
            )
            StatisticsPeriod.YEAR -> listOf(
                ChartData("Oca", 70f),
                ChartData("Şub", 73f),
                ChartData("Mar", 78f),
                ChartData("Nis", 82f),
                ChartData("May", 85f),
                ChartData("Haz", 88f),
                ChartData("Tem", 91f),
                ChartData("Ağu", 89f),
                ChartData("Eyl", 92f),
                ChartData("Eki", 88f),
                ChartData("Kas", 90f),
                ChartData("Ara", 93f)
            )
        }
    }
}

