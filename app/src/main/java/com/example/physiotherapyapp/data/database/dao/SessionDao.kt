package com.example.physiotherapyapp.data.database.dao

import androidx.room.*
import com.example.physiotherapyapp.data.model.ExerciseResult
import com.example.physiotherapyapp.data.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    
    @Query("SELECT * FROM sessions WHERE userId = :userId ORDER BY startTime DESC")
    fun getSessionsByUser(userId: String): Flow<List<Session>>
    
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: String): Session?
    
    @Query("SELECT * FROM sessions WHERE userId = :userId AND isCompleted = 1 ORDER BY startTime DESC LIMIT :limit")
    fun getRecentCompletedSessions(userId: String, limit: Int = 10): Flow<List<Session>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)
    
    @Update
    suspend fun updateSession(session: Session)
    
    @Delete
    suspend fun deleteSession(session: Session)
    
    @Query("SELECT * FROM exercise_results WHERE sessionId = :sessionId")
    fun getExerciseResultsBySession(sessionId: String): Flow<List<ExerciseResult>>
    
    @Query("SELECT * FROM exercise_results WHERE exerciseId = :exerciseId ORDER BY id DESC LIMIT :limit")
    fun getExerciseResultsByExercise(exerciseId: String, limit: Int = 10): Flow<List<ExerciseResult>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseResult(result: ExerciseResult)
    
    @Query("SELECT AVG(accuracy) FROM exercise_results WHERE exerciseId IN (SELECT id FROM exercises WHERE id = :exerciseId)")
    suspend fun getAverageAccuracyForExercise(exerciseId: String): Float
}

