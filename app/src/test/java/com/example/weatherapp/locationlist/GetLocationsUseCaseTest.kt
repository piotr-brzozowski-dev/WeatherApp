package com.example.weatherapp.locationlist

import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.geo.GeoLocation
import com.example.weatherapp.geo.GeoService
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class GetLocationsUseCaseTest {

    private val geoService = relaxedMock<GeoService>()
    private val locationDataStore = relaxedMock<LocationDataStore>()
    private val useCase = GetLocationsUseCase(geoService, locationDataStore)

    @Test
    fun `when fetching locations without current location available then stored list should be returned`() =
        runTest {
            val locationList = listOf(
                Location("Test1", 1.0, 1.0),
                Location("Test2", 1.0, 1.0)
            )
            coEvery { geoService.getLocation() } returns null
            coEvery { locationDataStore.getLocations() } returns flowOf(locationList)

            val results = useCase.execute()

            results shouldBe locationList
        }

    @Test
    fun `when fetching locations with current location available then stored list should be returned`() =
        runTest {
            val locationList = listOf(
                Location("Test1", 1.0, 1.0),
                Location("Test2", 1.0, 1.0)
            )
            coEvery { geoService.getLocation() } returns GeoLocation(1.0, 1.0)
            coEvery { locationDataStore.getLocations() } returns flowOf(locationList)

            val results = useCase.execute()

            results shouldBe (listOf(Location("Current location", 1.0, 1.0)) + locationList)
        }

    @Test
    fun `when fetching locations without current location available and without stored items then empty list should be returned`() =
        runTest {
            coEvery { geoService.getLocation() } returns null
            coEvery { locationDataStore.getLocations() } returns flowOf(emptyList())

            val results = useCase.execute()

            results shouldBe emptyList()
        }
}