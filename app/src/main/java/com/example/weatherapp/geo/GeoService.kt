package com.example.weatherapp.geo

import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoService @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val cancellationTokenSource: CancellationTokenSource,
    private val locationPermissionCheck: LocationPermissionCheck,
    private val geoLocationMapper: GeoLocationMapper
) {


    suspend fun getLocation(): GeoLocation? = try {
        if (
            locationPermissionCheck.checkAccessCourseLocation() != PERMISSION_GRANTED
        ) {
            null
        } else {
            fusedLocationProviderClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                .await()
                ?.let(geoLocationMapper::map)
        }
    } catch (ignore: CancellationException) {
        null
    }
}