package com.example.weatherapp.locationlist

import com.example.weatherapp.geo.GeoService
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val geoService: GeoService,
    private val locationDataStore: LocationDataStore
) {

    suspend fun execute(): List<Location> {
        val locationList = geoService.getLocation()?.let {
            val location = Location(
                name = "Current location",
                latitude = it.latitude,
                longitude = it.longitude
            )
            mutableListOf(location)
        } ?: mutableListOf()
        locationList.addAll(
            locationDataStore.getLocations().firstOrNull() ?: emptyList()
        )
        return locationList
    }
}