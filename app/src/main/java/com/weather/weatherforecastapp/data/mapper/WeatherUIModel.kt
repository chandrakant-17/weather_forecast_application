package com.weather.weatherforecastapp.data.mapper

import com.weather.weatherforecastapp.data.local.entity.WeatherEntity
import com.weather.weatherforecastapp.data.remote.dto.WeatherResponse

data class WeatherUIModel(
    val dateTime: String,
    val temp: Double,
    val description: String,
    val icon: String
)

fun WeatherResponse.toUI(): List<WeatherUIModel> {
    return list.map {
        WeatherUIModel(
            dateTime = it.dt_txt,
            temp = it.main.temp,
            description = it.weather.firstOrNull()?.description ?: "",
            icon = it.weather.firstOrNull()?.icon ?: ""
        )
    }
}

fun List<WeatherEntity>.toUI(): List<WeatherUIModel> {
    return map {
        WeatherUIModel(
            dateTime = it.dateTime,
            temp = it.temp,
            description = it.description,
            icon = it.icon
        )
    }
}