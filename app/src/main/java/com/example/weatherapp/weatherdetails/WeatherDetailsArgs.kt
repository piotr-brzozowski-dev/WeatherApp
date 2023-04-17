package com.example.weatherapp.weatherdetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDetailsArgs(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val source: WeatherDetailsSource
) : Parcelable

enum class WeatherDetailsSource {
    SEARCH, HOME
}