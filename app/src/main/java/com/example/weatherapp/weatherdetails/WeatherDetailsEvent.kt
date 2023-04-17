package com.example.weatherapp.weatherdetails

internal sealed class WeatherDetailsEvent {
    data class FetchWeatherDetailsFailed(val message: String?) : WeatherDetailsEvent()
    object CloseScreen : WeatherDetailsEvent()
}