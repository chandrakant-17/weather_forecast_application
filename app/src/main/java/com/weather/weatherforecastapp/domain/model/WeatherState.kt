package com.weather.weatherforecastapp.domain.model

import com.weather.weatherforecastapp.data.mapper.WeatherUIModel

data class WeatherState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val data: List<WeatherUIModel> = listOf(WeatherUIModel(dateTime = "", temp =0.00, description = "", icon = ""))
)