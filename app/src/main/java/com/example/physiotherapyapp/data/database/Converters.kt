package com.example.physiotherapyapp.data.database

import androidx.room.TypeConverter
import com.example.physiotherapyapp.data.model.SensorReading
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
    
    @TypeConverter
    fun fromSensorReadingList(value: List<SensorReading>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toSensorReadingList(value: String): List<SensorReading> {
        val listType = object : TypeToken<List<SensorReading>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

