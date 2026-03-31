package com.weather.weatherforecastapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weather.weatherforecastapp.presentation.screen.SplashScreen
import com.weather.weatherforecastapp.presentation.screen.WeatherAppScreen

@Composable
fun NavigationController(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route // Start screen is the Splash Screen
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.WeatherAppScreen.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            })
        }

        composable(Screen.WeatherAppScreen.route) {
            WeatherAppScreen()
        }
    }
}