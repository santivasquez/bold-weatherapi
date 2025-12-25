package com.example.bold_weather_api.di

import com.example.bold_weather_api.data.repository.MockWeatherRepository
import com.example.bold_weather_api.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(): WeatherRepository = MockWeatherRepository()
}


