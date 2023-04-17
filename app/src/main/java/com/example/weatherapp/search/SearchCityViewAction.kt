package com.example.weatherapp.search

sealed class SearchCityViewAction {
    data class SearchCityForResults(val searchPhrase: String): SearchCityViewAction()
    data class GoToWeatherDetails(
        val name: String,
        val latitude: Double,
        val longitude: Double
    ): SearchCityViewAction()
}