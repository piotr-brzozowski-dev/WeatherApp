package com.example.weatherapp.splash

internal sealed class SplashViewState {
    data class Initialized(val permissionGranted: Boolean) : SplashViewState()
    object Loading : SplashViewState()
}