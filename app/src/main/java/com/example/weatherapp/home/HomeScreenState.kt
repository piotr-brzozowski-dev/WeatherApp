package com.example.weatherapp.home

import com.example.weatherapp.weatherdetails.WeatherBasicInfo

internal sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Loaded(val savedLocation: List<WeatherBasicInfo>) : HomeScreenState()
}