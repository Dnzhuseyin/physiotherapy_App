package com.example.physiotherapyapp.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    
    private val _leaderboardData = MutableStateFlow<List<LeaderboardUser>>(emptyList())
    val leaderboardData: StateFlow<List<LeaderboardUser>> = _leaderboardData.asStateFlow()
    
    private val _currentUser = MutableStateFlow<LeaderboardUser?>(null)
    val currentUser: StateFlow<LeaderboardUser?> = _currentUser.asStateFlow()
    
    private val _selectedPeriod = MutableStateFlow(LeaderboardPeriod.MONTHLY)
    val selectedPeriod: StateFlow<LeaderboardPeriod> = _selectedPeriod.asStateFlow()
    
    init {
        loadLeaderboardData()
    }
    
    fun selectPeriod(period: LeaderboardPeriod) {
        _selectedPeriod.value = period
        loadLeaderboardData()
    }
    
    private fun loadLeaderboardData() {
        viewModelScope.launch {
            // Generate sample leaderboard data
            val sampleUsers = generateSampleLeaderboardData()
            _leaderboardData.value = sampleUsers
            
            // Set current user (for demo, using a specific user)
            _currentUser.value = sampleUsers.find { it.id == "current_user" }
        }
    }
    
    private fun generateSampleLeaderboardData(): List<LeaderboardUser> {
        val users = listOf(
            LeaderboardUser("user1", "Ahmet Yılmaz", 2150, 8, 92.5f, 1),
            LeaderboardUser("user2", "Ayşe Demir", 1980, 7, 89.3f, 2),
            LeaderboardUser("user3", "Mehmet Kaya", 1875, 6, 91.2f, 3),
            LeaderboardUser("current_user", "Sen", 1250, 5, 85.5f, 4),
            LeaderboardUser("user5", "Fatma Özkan", 1180, 5, 88.7f, 5),
            LeaderboardUser("user6", "Ali Şahin", 1120, 4, 84.2f, 6),
            LeaderboardUser("user7", "Zeynep Acar", 1050, 4, 86.9f, 7),
            LeaderboardUser("user8", "Mustafa Çelik", 980, 4, 83.1f, 8),
            LeaderboardUser("user9", "Elif Yıldız", 920, 3, 87.4f, 9),
            LeaderboardUser("user10", "Osman Koç", 890, 3, 82.8f, 10),
            LeaderboardUser("user11", "Seda Arslan", 850, 3, 85.6f, 11),
            LeaderboardUser("user12", "Burak Doğan", 820, 3, 81.9f, 12),
            LeaderboardUser("user13", "Gamze Polat", 780, 2, 84.3f, 13),
            LeaderboardUser("user14", "Emre Güneş", 750, 2, 80.7f, 14),
            LeaderboardUser("user15", "Derya Kılıç", 720, 2, 83.5f, 15)
        )
        
        return when (_selectedPeriod.value) {
            LeaderboardPeriod.WEEKLY -> users.shuffled().take(15)
                .mapIndexed { index, user -> user.copy(rank = index + 1) }
                .sortedBy { it.rank }
            LeaderboardPeriod.MONTHLY -> users
            LeaderboardPeriod.ALL_TIME -> users.map { user ->
                user.copy(points = (user.points * 1.5).toInt())
            }.sortedByDescending { it.points }
                .mapIndexed { index, user -> user.copy(rank = index + 1) }
        }
    }
}

