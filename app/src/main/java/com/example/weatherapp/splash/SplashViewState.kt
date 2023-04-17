package com.example.weatherapp.splash

internal sealed class SplashViewState {
    object Initialized : SplashViewState()
    object Loading : SplashViewState()
    object FailedToInit : SplashViewState()
}