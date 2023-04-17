package com.example.weatherapp.geo

import android.content.Context
import com.example.weatherapp.foundation.OnAppCreate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface GeoModule {

    @Binds
    @IntoSet
    fun LocationPermissionService.bindToOnAppCreate(): OnAppCreate

    companion object {
        @Provides
        fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        @Provides
        fun provideCancellationTokenSource(): CancellationTokenSource = CancellationTokenSource()
    }
}