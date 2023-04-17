package com.example.weatherapp.locationlist

import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationDataStore: LocationDataStore,
    private val locationRepositoryConfig: LocationRepositoryConfig
) {
    suspend fun execute(location: Location) {
        val locations = locationDataStore.getLocations().firstOrNull()
        val locationLimit = locationRepositoryConfig.locationLimit
        if (locationLimit < (locations?.size ?: 0)) {
            throw IllegalArgumentException("Can't add more location than $locationLimit")
        }
        val possibleToAdd =
            locations?.find { it.latitude == location.latitude && it.longitude == location.longitude } == null

        if (possibleToAdd) {
            locationDataStore.saveLocation(location)
        }
    }
}