package com.weather.weatherforecastapp.domain.usecase

import com.weather.weatherforecastapp.data.local.dao.WeatherDao
import com.weather.weatherforecastapp.data.local.entity.WeatherEntity
import com.weather.weatherforecastapp.data.mapper.WeatherUIModel
import com.weather.weatherforecastapp.data.mapper.toUI
import com.weather.weatherforecastapp.data.remote.dto.WeatherResponse
import com.weather.weatherforecastapp.data.repository.WeatherRepository
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRoomRepository,
) {
    suspend operator fun invoke(city: String): ServiceResult<List<WeatherUIModel>> {
        return try {
            val apiData = repository.getWeather(city)
            ServiceResult.Success(apiData.toUI())

        } catch (e: Exception) {

            val localData = repository.getSavedWeather(city)

            if (localData.isNotEmpty()) {
                ServiceResult.Success(localData.toUI())
            } else {
                ServiceResult.Error(e.message ?: "No data available")
            }
        }
    }

}

class WeatherRoomRepository @Inject constructor(
    private val api: WeatherRepository,
    private val dao: WeatherDao
) {

    suspend fun getWeather(city: String): WeatherResponse {
        val response = api.getWeather(city)

        dao.deleteWeather(city)
        dao.insertWeather(response.toEntity(city))

        return response
    }

    suspend fun getSavedWeather(city: String): List<WeatherEntity> {
        return dao.getWeather(city)
    }
}



sealed class ServiceResult<out T> {
    object Loading : ServiceResult<Nothing>()
    data class Success<out T>(val data: T) : ServiceResult<T>()
    data class Error(val message:String) : ServiceResult<Nothing>()
}

fun WeatherResponse.toEntity(city: String): List<WeatherEntity> {
    return list.map {
        WeatherEntity(
            dateTime = it.dt_txt,
            temp = it.main.temp,
            description = it.weather.firstOrNull()?.description ?: "",
            icon = it.weather.firstOrNull()?.icon ?: "",
            city = city
        )
    }
}