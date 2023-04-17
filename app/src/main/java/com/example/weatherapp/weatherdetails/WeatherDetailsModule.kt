package com.example.weatherapp.weatherdetails

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WeatherDetailsModule {

    @Provides
    @Singleton
    fun provideWeatherDetailsApi(retrofit: Retrofit): WeatherDetailsApi =
        retrofit.create(WeatherDetailsApi::class.java)
}