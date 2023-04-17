package com.example.weatherapp.locationlist

import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class DeleteLocationUseCaseTest {

    private val locationDataStore = relaxedMock<LocationDataStore>()
    private val useCase = DeleteLocationUseCase(locationDataStore)

    @Test
    fun `when removing location using use case then location should be removed from data store`() = runTest {
        val location = Location("test", 1.0, 1.0)

        useCase.execute(location)

        coVerify { locationDataStore.removeLocation(location) }
    }
}