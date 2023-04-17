package com.example.weatherapp.search

internal sealed class SearchCityViewEvent {
    data class NavigateToWeatherDetails(
        val name: String,
        val latitude: Double,
        val longitude: Double,
    ) : SearchCityViewEvent()
}