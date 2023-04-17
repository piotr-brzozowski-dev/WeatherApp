package com.example.weatherapp.home

sealed class HomeScreenEvent {

    object NavigateToSearch: HomeScreenEvent()
    data class NavigateToWeatherDetails(
        val name: String,
        val latitude: Double,
        val longitude: Double,
    ): HomeScreenEvent()
}