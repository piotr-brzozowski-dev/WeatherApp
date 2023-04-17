package com.example.weatherapp.geo

import android.location.Location
import javax.inject.Inject

class GeoLocationMapper @Inject constructor() {

    fun map(location: Location): GeoLocation = with(location) {
        GeoLocation(
            latitude = latitude,
            longitude = longitude
        )
    }
}