package com.weather.weatherforecastapp.data.repository

import com.weather.weatherforecastapp.data.remote.api.WeatherApi
import com.weather.weatherforecastapp.data.remote.dto.WeatherResponse
import com.weather.weatherforecastapp.domain.utils.Constants.API_KEY

class WeatherRepositoryImpl(
    private val api: WeatherApi,
) : WeatherRepository {

    override suspend fun getWeather(city: String): WeatherResponse {
        return api.getWeatherForecast(city,apiKey = API_KEY)
    }
}