package com.example.physiotherapyapp.data.database.dao

import androidx.room.*
import com.example.physiotherapyapp.data.model.BodyPart
import com.example.physiotherapyapp.data.model.Exercise
import com.example.physiotherapyapp.data.model.ExerciseDifficulty
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: String): Exercise?
    
    @Query("SELECT * FROM exercises WHERE bodyPart = :bodyPart")
    fun getExercisesByBodyPart(bodyPart: BodyPart): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE difficulty = :difficulty")
    fun getExercisesByDifficulty(difficulty: ExerciseDifficulty): Flow<List<Exercise>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)
    
    @Update
    suspend fun updateExercise(exercise: Exercise)
    
    @Delete
    suspend fun deleteExercise(exercise: Exercise)
    
    @Query("DELETE FROM exercises")
    suspend fun deleteAllExercises()
}

