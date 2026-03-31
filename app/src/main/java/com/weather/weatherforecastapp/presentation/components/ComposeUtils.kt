package com.weather.weatherforecastapp.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weather.weatherforecastapp.data.mapper.WeatherUIModel


@Composable
fun WeatherIcon(iconCode: String) {
    val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(iconUrl).crossfade(true).build(),
        contentDescription = null,
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun getWeatherCardBackgroundColor(forecast: WeatherUIModel): Color {
    return when (forecast.description.lowercase()) {
        "clear sky" -> Color(0xFF81D4FA)
        "few clouds" -> Color(0xFFB3E5FC)
        "scattered clouds" -> Color(0xFF90CAF9)
        "rain" -> Color(0xFF90A4AE)
        "overcast clouds" -> Color(0xFF607D8B)
        else -> Color(0xFFE3F2FD)
    }
}