package com.example.weatherapp.geo

import android.location.Location
import androidx.core.content.PermissionChecker
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class GeoServiceTest {

    private val cancellationToken = relaxedMock<CancellationToken>()
    private val fusedLocationProviderClient = relaxedMock<FusedLocationProviderClient>()
    private val cancellationTokenSource = relaxedMock<CancellationTokenSource> {
        every { token } returns cancellationToken
    }
    private val locationPermissionCheck = relaxedMock<LocationPermissionCheck>()
    private val getLocationMapper = relaxedMock<GeoLocationMapper>()

    private val service = GeoService(
        fusedLocationProviderClient,
        cancellationTokenSource,
        locationPermissionCheck,
        getLocationMapper
    )

    @Test
    fun `when permission is not granted then null location should be returned`() = runTest {
        every { locationPermissionCheck.checkAccessCourseLocation() } returns PermissionChecker.PERMISSION_DENIED

        val result = service.getLocation()

        result shouldBe null
    }

    @Test
    fun `when permission is granted then provided location should be returned`() = runTest {
        val location = relaxedMock<Location> {
            every { latitude } returns 1.0
            every { longitude } returns 1.0
        }
        val geoLocation = GeoLocation(1.0, 1.0)
        val locationTask = relaxedMock<Task<Location>> {
            every { isComplete } returns true
            every { isCanceled } returns false
            every { exception } returns null
            coEvery { result } returns location
        }
        every { getLocationMapper.map(location) } returns geoLocation
        every { locationPermissionCheck.checkAccessCourseLocation() } returns PermissionChecker.PERMISSION_GRANTED
        every {
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
        } returns locationTask

        val result = service.getLocation()

        result shouldBe geoLocation
    }
}