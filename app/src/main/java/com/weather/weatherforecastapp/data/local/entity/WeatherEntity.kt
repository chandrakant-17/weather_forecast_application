package com.weather.weatherforecastapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateTime: String,
    val temp: Double,
    val description: String,
    val icon: String,
    val city: String
)