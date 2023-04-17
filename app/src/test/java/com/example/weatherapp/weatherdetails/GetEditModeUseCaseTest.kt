package com.example.weatherapp.weatherdetails

import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.locationlist.Location
import com.example.weatherapp.locationlist.LocationDataStore
import com.example.weatherapp.locationlist.LocationRepositoryConfig
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class GetEditModeUseCaseTest {

    private val locationDataStore = relaxedMock<LocationDataStore>()
    private val locationRepositoryConfig = LocationRepositoryConfig(2)
    private val getEditModeUseCase = GetEditModeUseCase(locationDataStore, locationRepositoryConfig)

    @Test
    fun `when executing use case without any item in the list then ADD edit mode should be returned`() =
        runTest {
            val weatherDetails = relaxedMock<WeatherDetails>()

            val result = getEditModeUseCase.execute(weatherDetails)

            result shouldBe EditMode.ADD
        }

    @Test
    fun `when executing use case and item exists in store then DELETE edit mode should be returned`() =
        runTest {
            val weatherDetails = relaxedMock<WeatherDetails> {
                every { cityName } returns "Test"
                every { latitude } returns 1.0
                every { longitude } returns 1.0
            }
            every { locationDataStore.getLocations() } returns flowOf(
                listOf(
                    Location("Test", 1.0, 1.0),
                    Location("Test1", 1.0, 1.0)
                )
            )

            val result = getEditModeUseCase.execute(weatherDetails)

            result shouldBe EditMode.DELETE
        }

    @Test
    fun `when executing use case and item list is full then READ_ONLY edit mode should be returned`() =
        runTest {
            val weatherDetails = relaxedMock<WeatherDetails>()
            every { locationDataStore.getLocations() } returns flowOf(
                listOf(
                    Location("Test", 1.0, 1.0),
                    Location("Test1", 1.0, 1.0)
                )
            )

            val result = getEditModeUseCase.execute(weatherDetails)

            result shouldBe EditMode.READ_ONLY
        }
}