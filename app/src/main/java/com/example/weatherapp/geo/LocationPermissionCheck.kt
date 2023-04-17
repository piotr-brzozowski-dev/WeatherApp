package com.example.weatherapp.geo

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import androidx.annotation.CheckResult
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PermissionResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationPermissionCheck @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @CheckResult
    @PermissionResult
    fun checkAccessCourseLocation(): Int = ContextCompat
        .checkSelfPermission(context, ACCESS_COARSE_LOCATION)
}