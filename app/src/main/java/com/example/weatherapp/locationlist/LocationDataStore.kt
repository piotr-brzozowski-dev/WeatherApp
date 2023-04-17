package com.example.weatherapp.locationlist

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

class LocationDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveLocation(location: Location) = dataStore.edit { perferences ->
        val encoded = Json.encodeToString(serializer(), location)
        val locationSet = perferences[locationKey]
        val updatedLocationSet = locationSet?.toMutableSet()?.also {
            it.add(encoded)
        } ?: mutableSetOf(encoded)
        perferences[locationKey] = updatedLocationSet
    }

    suspend fun removeLocation(location: Location) {
        val locations = getLocations().firstOrNull() ?: mutableListOf()
        val updatedLocations = locations.filterNot { it == location }.map {
            Json.encodeToString(serializer(), it)
        }.toSet()
        dataStore.edit { preferences ->
            preferences[locationKey] = updatedLocations
        }
    }

    internal fun getLocations(): Flow<List<Location>> =
        dataStore.data.map { preferences ->
            preferences[locationKey]
        }.map { locationSet ->
            locationSet?.map { locationEncoded ->
                Json.decodeFromString(serializer<Location>(), locationEncoded)
            }?.toList() ?: emptyList()
        }

    companion object {
        private const val LOCATION_KEY = "LOCATION_KEY"
        val locationKey = stringSetPreferencesKey(LOCATION_KEY)
    }
}