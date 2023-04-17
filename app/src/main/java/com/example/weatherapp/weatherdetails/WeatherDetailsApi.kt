package com.example.weatherapp.weatherdetails

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDetailsApi {

    @GET("https://api.open-meteo.com/v1/forecast?daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,rain_sum,windspeed_10m_max&current_weather=true&forecast_days=10&timezone=auto")
    suspend fun getWeatherDetails(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): WeatherDetailsDto

    @GET("https://api.open-meteo.com/v1/forecast?current_weather=true&timezone=auto&daily=temperature_2m_max")
    suspend fun getWeatherBasicInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): WeatherBasicInfoDto
}