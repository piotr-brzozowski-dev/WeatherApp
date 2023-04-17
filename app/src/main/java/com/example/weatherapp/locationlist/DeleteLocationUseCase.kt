package com.example.weatherapp.locationlist

import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(private val locationDataStore: LocationDataStore) {

    suspend fun execute(location: Location) {
        locationDataStore.removeLocation(location)
    }
}