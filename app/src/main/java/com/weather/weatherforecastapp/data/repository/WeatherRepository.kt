package com.weather.weatherforecastapp.data.repository

import com.weather.weatherforecastapp.data.remote.dto.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse
}