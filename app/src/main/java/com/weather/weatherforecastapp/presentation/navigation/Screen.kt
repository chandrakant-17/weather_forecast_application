package com.weather.weatherforecastapp.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object WeatherAppScreen : Screen("weatherApp")
}