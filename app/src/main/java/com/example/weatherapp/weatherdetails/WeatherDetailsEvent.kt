package com.example.weatherapp.weatherdetails

sealed class WeatherDetailsEvent {
    data class FetchWeatherDetailsFailed(val message: String?): WeatherDetailsEvent()
    object CloseScreen: WeatherDetailsEvent()
}