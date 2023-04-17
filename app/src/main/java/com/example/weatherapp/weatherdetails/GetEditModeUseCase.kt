package com.example.weatherapp.weatherdetails

import com.example.weatherapp.locationlist.LocationDataStore
import com.example.weatherapp.locationlist.LocationRepositoryConfig
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class GetEditModeUseCase @Inject constructor(
    private val locationDataStore: LocationDataStore,
    private val locationRepositoryConfig: LocationRepositoryConfig
) {
    suspend fun execute(weatherDetails: WeatherDetails): EditMode {
        val locationList = locationDataStore.getLocations().firstOrNull() ?: emptyList()
        val isLocationListFull = locationList.size >= locationRepositoryConfig.locationLimit
        val isItemOnTheList = locationList.firstOrNull {
            it.latitude == weatherDetails.latitude && it.longitude == weatherDetails.longitude
        } != null
        return when {
            isItemOnTheList -> EditMode.DELETE
            isLocationListFull -> EditMode.READ_ONLY
            else -> EditMode.ADD
        }
    }
}