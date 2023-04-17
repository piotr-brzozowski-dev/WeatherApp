package com.example.weatherapp.home

sealed class HomeScreenAction {

    object LoadScreen : HomeScreenAction()
    object GoToSearchScreen : HomeScreenAction()
    data class GoToWeatherDetails(
        val name: String,
        val latitude: Double,
        val longitude: Double
    ): HomeScreenAction()
}