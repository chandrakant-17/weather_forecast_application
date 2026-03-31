package com.weather.weatherforecastapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.weatherforecastapp.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(list: List<WeatherEntity>)

    @Query("SELECT * FROM weather_table WHERE city = :city")
    suspend fun getWeather(city: String): List<WeatherEntity>

    @Query("DELETE FROM weather_table WHERE city = :city")
    suspend fun deleteWeather(city: String)
}