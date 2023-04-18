package com.example.weatherapp.locationlist

import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class AddLocationUseCaseTest {

    private val locationDataStore = relaxedMock<LocationDataStore>()
    private val locationRepositoryConfig = LocationRepositoryConfig(2)

    private val addLocationUseCase = AddLocationUseCase(locationDataStore, locationRepositoryConfig)

    @Test
    fun `when adding location to empty list then location should be stored`() = runTest {
        val location = Location("Test", 1.0, 1.0)
        coEvery { locationDataStore.getLocations() } returns flowOf(emptyList())

        addLocationUseCase.execute(location)

        coVerify { locationDataStore.saveLocation(location) }
    }

    @Test
    fun `when adding location to not full list then location should be stored`() = runTest {
        val location = Location("Test", 2.0, 2.0)
        coEvery { locationDataStore.getLocations() } returns flowOf(
            listOf(
                Location(
                    "Test1",
                    1.0,
                    1.0
                )
            )
        )

        addLocationUseCase.execute(location)

        coVerify { locationDataStore.saveLocation(location) }
    }

    @Test
    fun `when adding location to full list then exception should be thrown`() = runTest {
        val location = Location("Test", 3.0, 3.0)
        coEvery { locationDataStore.getLocations() } returns flowOf(
            listOf(
                Location("Test1", 1.0, 1.0),
                Location("Test2", 2.0, 2.0)
            )
        )

        shouldThrow<IllegalArgumentException> {
            addLocationUseCase.execute(location)
        }
    }
}