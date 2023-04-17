package com.example.weatherapp.weatherdetails

internal sealed class WeatherDetailsViewState {
    object Loading : WeatherDetailsViewState()
    data class Loaded(
        val weatherDetails: WeatherDetails,
        val editMode: EditMode,
    ) : WeatherDetailsViewState()
}