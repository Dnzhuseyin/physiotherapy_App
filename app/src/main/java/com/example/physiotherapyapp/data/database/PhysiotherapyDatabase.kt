package com.example.physiotherapyapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.physiotherapyapp.data.database.dao.ExerciseDao
import com.example.physiotherapyapp.data.database.dao.SessionDao
import com.example.physiotherapyapp.data.database.dao.UserDao
import com.example.physiotherapyapp.data.model.*

@Database(
    entities = [
        Exercise::class,
        Session::class,
        ExerciseResult::class,
        User::class,
        Achievement::class,
        UserAchievement::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PhysiotherapyDatabase : RoomDatabase() {
    
    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao
    
    companion object {
        const val DATABASE_NAME = "physiotherapy_database"
        
        @Volatile
        private var INSTANCE: PhysiotherapyDatabase? = null
        
        fun getDatabase(context: Context): PhysiotherapyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhysiotherapyDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

