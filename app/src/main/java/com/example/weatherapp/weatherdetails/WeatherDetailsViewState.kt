package com.example.weatherapp.weatherdetails

sealed class WeatherDetailsViewState {
    object Loading: WeatherDetailsViewState()
    data class Loaded(
        val weatherDetails: WeatherDetails,
        val editMode: EditMode,
    ): WeatherDetailsViewState()
}