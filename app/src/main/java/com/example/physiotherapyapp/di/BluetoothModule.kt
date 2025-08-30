package com.example.physiotherapyapp.di

import android.content.Context
import com.example.physiotherapyapp.data.bluetooth.PhysiotherapyBluetoothManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {
    
    @Provides
    @Singleton
    fun providePhysiotherapyBluetoothManager(
        @ApplicationContext context: Context
    ): PhysiotherapyBluetoothManager {
        return PhysiotherapyBluetoothManager(context)
    }
}

