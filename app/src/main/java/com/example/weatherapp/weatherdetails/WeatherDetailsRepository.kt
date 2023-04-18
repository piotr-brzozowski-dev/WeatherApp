package com.example.weatherapp.weatherdetails

import com.example.weatherapp.runSuspendCatching
import javax.inject.Inject

class WeatherDetailsRepository @Inject constructor(
    private val weatherDetailsApi: WeatherDetailsApi,
    private val weatherDetailsMapper: WeatherDetailsMapper
) {

    suspend fun getWeatherDetails(cityName: String, latitude: Double, longitude: Double) =
        runSuspendCatching {
            val results = weatherDetailsApi.getWeatherDetails(
                latitude, longitude
            )
            weatherDetailsMapper.map(cityName, results)
        }

    suspend fun getWeatherBasicInfo(cityName: String, latitude: Double, longitude: Double) =
        runSuspendCatching {
            val results = weatherDetailsApi.getWeatherBasicInfo(latitude, longitude)
            weatherDetailsMapper.map(cityName, results)
        }
}