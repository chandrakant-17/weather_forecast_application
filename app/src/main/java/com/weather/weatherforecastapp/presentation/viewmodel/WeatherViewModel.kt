package com.weather.weatherforecastapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.weatherforecastapp.data.mapper.WeatherUIModel
import com.weather.weatherforecastapp.domain.model.WeatherState
import com.weather.weatherforecastapp.domain.usecase.ServiceResult
import com.weather.weatherforecastapp.domain.usecase.WeatherUseCase
import com.weather.weatherforecastapp.domain.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {

    private val _state = mutableStateOf(WeatherState())
    val state: State<WeatherState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            when (val result = weatherUseCase.invoke(city)) {
                is ServiceResult.Loading -> {
                    _state.value = WeatherState(isLoading = true)
                    _eventFlow.emit(UiEvent.Loading)

                }

                is ServiceResult.Success -> {
                    Log.e("TAG", "fetchWeather: $result")
                    _state.value = WeatherState(
                        data = getWeatherForSpecificDays(result.data),
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.Success)

                }

                is ServiceResult.Error -> {
                    _state.value = WeatherState(isError = true, isLoading = false)

                    Log.e("TAG", "Error: $result")
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = result.message))

                }
            }
        }
    }

    private fun getWeatherForSpecificDays(weatherList: List<WeatherUIModel>): List<WeatherUIModel> {
        val calendar = Calendar.getInstance()

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val today = sdf.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = sdf.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val dayAfterTomorrow = sdf.format(calendar.time)
        val groupedByDate = weatherList.groupBy { it.dateTime.substring(0, 10) }
        val filteredWeather = groupedByDate.filter { (date, _) ->
            date == today || date == tomorrow || date == dayAfterTomorrow
        }.map { (_, forecasts) ->
            val forecast = forecasts.first()

            forecast.copy(dateTime = forecast.dateTime.take(10))
        }

        return filteredWeather
    }

}