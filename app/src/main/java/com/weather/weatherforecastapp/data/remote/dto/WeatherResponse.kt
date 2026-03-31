package com.weather.weatherforecastapp.data.remote.dto

data class WeatherResponse(
    val list: List<WeatherItem>
)

data class WeatherItem(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String,
    val icon: String
)