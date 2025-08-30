package com.example.physiotherapyapp.di

import android.content.Context
import androidx.room.Room
import com.example.physiotherapyapp.data.database.PhysiotherapyDatabase
import com.example.physiotherapyapp.data.database.dao.ExerciseDao
import com.example.physiotherapyapp.data.database.dao.SessionDao
import com.example.physiotherapyapp.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun providePhysiotherapyDatabase(
        @ApplicationContext context: Context
    ): PhysiotherapyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PhysiotherapyDatabase::class.java,
            PhysiotherapyDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideExerciseDao(database: PhysiotherapyDatabase): ExerciseDao {
        return database.exerciseDao()
    }
    
    @Provides
    fun provideSessionDao(database: PhysiotherapyDatabase): SessionDao {
        return database.sessionDao()
    }
    
    @Provides
    fun provideUserDao(database: PhysiotherapyDatabase): UserDao {
        return database.userDao()
    }
}

