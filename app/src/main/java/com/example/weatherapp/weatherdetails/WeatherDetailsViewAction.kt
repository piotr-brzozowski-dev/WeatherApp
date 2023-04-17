package com.example.weatherapp.weatherdetails

internal sealed class WeatherDetailsViewAction {
    object LoadWeatherDetails : WeatherDetailsViewAction()
    data class AddLocation(val locationName: String, val latitude: Double, val longitude: Double) :
        WeatherDetailsViewAction()

    data class DeleteLocation(
        val locationName: String,
        val latitude: Double,
        val longitude: Double
    ) : WeatherDetailsViewAction()
}