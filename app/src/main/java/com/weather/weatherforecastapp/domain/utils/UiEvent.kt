package com.weather.weatherforecastapp.domain.utils


sealed class UiEvent : Event() {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Success : UiEvent()
    object Loading : UiEvent()
}

abstract class Event