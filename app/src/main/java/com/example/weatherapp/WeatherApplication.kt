package com.example.weatherapp

import android.app.Application
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class WeatherApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppDependenciesEntryPoint {

        fun initializer(): AppInitializer
    }

    private val appDependencies get() = EntryPoints.get(this, AppDependenciesEntryPoint::class.java)

    override fun onCreate() {
        super.onCreate()
        appDependencies.initializer().init(this)
    }
}