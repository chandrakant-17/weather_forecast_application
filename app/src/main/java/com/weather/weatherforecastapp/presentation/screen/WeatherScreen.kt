package com.weather.weatherforecastapp.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.weatherforecastapp.data.mapper.WeatherUIModel
import com.weather.weatherforecastapp.domain.utils.UiEvent
import com.weather.weatherforecastapp.presentation.components.WeatherIcon
import com.weather.weatherforecastapp.presentation.components.getWeatherCardBackgroundColor
import com.weather.weatherforecastapp.presentation.components.noRippleClickable
import com.weather.weatherforecastapp.presentation.viewmodel.WeatherViewModel
import com.weather.weatherforecastapp.ui.theme.AccentBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val focusManager = LocalFocusManager.current
    val city = remember { mutableStateOf("") }
    val context = LocalContext.current
    val state = viewModel.state
    LaunchedEffect(true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    "Weather Forecast", style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF2196F3), Color(0xFF0D47A1)
                    )
                )
            ),
        )
    }, floatingActionButton = {
        if (city.value.isNotEmpty()) {
            FloatingActionButton(
                onClick = {
                    viewModel.fetchWeather(city.value)

                }, containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Weather",
                    tint = Color.White
                )
            }
        }
    }, content = { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .noRippleClickable {
                focusManager.clearFocus()
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE3F2FD), Color(0xFFBBDEFB)
                    )
                )
            )
            .padding(16.dp)

        ) {

            CitySearchBar(city, onSearch = { cityName ->
                if (cityName.isNotEmpty()) {
                    viewModel.fetchWeather(cityName)
                    focusManager.clearFocus()
                }
            }, focusManager)

            Spacer(modifier = Modifier.height(16.dp))

            if (state.value.isError) {
                Text(
                    text = state.value.errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (state.value.data.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(state.value.data) { _, forecast ->
                        Log.d("Weather", "Weather Data: ${forecast.dateTime}")
                        if (forecast.dateTime.isNotEmpty()) {
                            WeatherCard(forecast = forecast)
                        }
                    }
                }
            }
        }
    })
    if (state.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.width(70.dp),
                color = AccentBlue,
                strokeWidth = 3.dp,
                trackColor = Color.Gray
            )
        }
    }
}

@Composable
fun CitySearchBar(
    city: MutableState<String>,
    onSearch: (String) -> Unit,
    focusManager: FocusManager,
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = city.value,
            onValueChange = { city.value = it },
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = "Enter City",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (city.value.isNotEmpty()) {
                        IconButton(onClick = { city.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = "Clear"
                            )
                        }
                    }
                    IconButton(onClick = { onSearch(city.value) }) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "Search"
                        )
                    }
                }
            },
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch(city.value)
                    focusManager.clearFocus()
                }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
        )
    }
}

@Composable
fun WeatherCard(forecast: WeatherUIModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = getWeatherCardBackgroundColor(forecast)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherIcon(forecast.icon)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = forecast.dateTime, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = forecast.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "${forecast.temp}°C",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
