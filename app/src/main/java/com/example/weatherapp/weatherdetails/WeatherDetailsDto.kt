package com.example.weatherapp.weatherdetails

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailsDto(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto,
    @SerialName("daily_units") val dailyUnits: DailyUnitsDto,
    @SerialName("daily") val dailyWeather: DailyWeatherDto
)

@Serializable
data class WeatherBasicInfoDto(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto,
    @SerialName("daily_units") val dailyUnits: DailyUnitsDto,
)

@Serializable
data class CurrentWeatherDto(
    val temperature: Float,
    @SerialName("windspeed") val windSpeed: Float,
    @SerialName("winddirection") val windDirection: Float,
    @SerialName("time") val time: LocalDateTime,
    @SerialName("is_day") val isDay: Int
)

@Serializable
data class DailyUnitsDto(
    @SerialName("temperature_2m_max") val temperature: String,
    @SerialName("rain_sum") val rainSum: String? = null,
    @SerialName("windspeed_10m_max") val windSpeed: String? = null
)

@Serializable
data class DailyWeatherDto(
    val time: List<LocalDate>,
    @SerialName("temperature_2m_max") val maxTemperature: List<Float>,
    @SerialName("temperature_2m_min") val minTemperature: List<Float>,
    @SerialName("sunrise") val sunrise: List<LocalDateTime>,
    @SerialName("sunset") val sunset: List<LocalDateTime>,
    @SerialName("uv_index_max") val uvIndex: List<Float>,
    @SerialName("rain_sum") val rainSum: List<Float>,
    @SerialName("windspeed_10m_max") val windSpeed: List<Float>
)