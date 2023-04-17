package com.example.weatherapp.weatherdetails

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class WeatherDetails(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val currentWeather: CurrentWeather,
    val forecast: List<Forecast>,
    val units: Units
)

data class WeatherBasicInfo(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Float,
    val temperatureUnit: String,
)

data class CurrentWeather(
    val temperature: Float,
    val maxTemperature: Float,
    val minTemperature: Float,
    val windSpeed: Float,
    val windDirection: Float,
    val time: LocalDateTime,
    val isDay: Boolean,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val uvIndex: Float,
    val rainSum: Float,
)

data class Forecast(
    val time: LocalDate,
    val maxTemperature: Float,
    val minTemperature: Float,
    val rainSum: Float
)

data class Units(
    val temperatureUnit: String,
    val windSpeedUnit: String?,
    val rainSumUnit: String?
)