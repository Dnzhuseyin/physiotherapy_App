package com.example.physiotherapyapp.data.database.dao

import androidx.room.*
import com.example.physiotherapyapp.data.model.Achievement
import com.example.physiotherapyapp.data.model.User
import com.example.physiotherapyapp.data.model.UserAchievement
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): User?
    
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserByIdFlow(id: String): Flow<User?>
    
    @Query("SELECT * FROM users ORDER BY totalPoints DESC")
    fun getAllUsersByPoints(): Flow<List<User>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getAchievementById(id: String): Achievement?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<Achievement>)
    
    @Query("SELECT a.* FROM achievements a INNER JOIN user_achievements ua ON a.id = ua.achievementId WHERE ua.userId = :userId")
    fun getUserAchievements(userId: String): Flow<List<Achievement>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAchievement(userAchievement: UserAchievement)
    
    @Query("SELECT COUNT(*) FROM user_achievements WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun hasUserAchievement(userId: String, achievementId: String): Int
}

