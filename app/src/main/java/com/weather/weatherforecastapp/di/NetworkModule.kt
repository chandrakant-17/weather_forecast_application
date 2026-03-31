package com.weather.weatherforecastapp.di

import com.google.gson.GsonBuilder
import com.weather.weatherforecastapp.data.remote.api.WeatherApi
import com.weather.weatherforecastapp.data.repository.WeatherRepository
import com.weather.weatherforecastapp.data.repository.WeatherRepositoryImpl
import com.weather.weatherforecastapp.domain.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val loggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun providesPoleService(): WeatherApi {
        val baseUrl = Constants.BASE_URL
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun providePoleServices(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}