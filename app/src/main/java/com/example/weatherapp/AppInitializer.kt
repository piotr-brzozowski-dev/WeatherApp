package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.foundation.OnAppCreate
import javax.inject.Inject
import javax.inject.Provider

class AppInitializer @Inject constructor(private val appCreateActions: Provider<Set<OnAppCreate>>) {

    fun init(app: Application) {
        appCreateActions.get().onEach { it.onCreate(app) }
    }
}