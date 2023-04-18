package com.example.weatherapp.home

import app.cash.turbine.test
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.locationlist.GetLocationsUseCase
import com.example.weatherapp.locationlist.Location
import com.example.weatherapp.relaxedMock
import com.example.weatherapp.weatherdetails.*
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class HomeViewModelTest {

    private val getLocationsUseCase = relaxedMock<GetLocationsUseCase>()
    private val weatherDetailsRepository = relaxedMock<WeatherDetailsRepository>()

    private val viewModel = HomeViewModel(getLocationsUseCase, weatherDetailsRepository)

    @Test
    fun `when load data then home screen location list should be emitted`() = runTest {
        val weatherBasicInfo = relaxedMock<WeatherBasicInfo>()
        coEvery { getLocationsUseCase.execute() } returns listOf(
            Location("Test", 1.0, 1.0)
        )
        coEvery {
            weatherDetailsRepository.getWeatherBasicInfo("Test", 1.0, 1.0)
        } returns Result.success(weatherBasicInfo)

        viewModel.state.test {
            viewModel.onAction(HomeScreenAction.LoadScreen)

            awaitItem() shouldBe HomeScreenState.Loading
            awaitItem() shouldBe HomeScreenState.Loaded(listOf(weatherBasicInfo))
        }
    }

    @Test
    fun `when load data with error then empty home screen location list should be emitted`() =
        runTest {
            coEvery { getLocationsUseCase.execute() } returns listOf(
                Location("Test", 1.0, 1.0)
            )
            coEvery {
                weatherDetailsRepository.getWeatherBasicInfo("Test", 1.0, 1.0)
            } returns Result.failure(Exception("error"))

            viewModel.state.test {
                viewModel.onAction(HomeScreenAction.LoadScreen)

                awaitItem() shouldBe HomeScreenState.Loading
                awaitItem() shouldBe HomeScreenState.Loaded(emptyList())
            }
        }

    @Test
    fun `when going to search screen then navigation to search screen event should be emitted`() =
        runTest {
            viewModel.event.test {
                viewModel.onAction(HomeScreenAction.GoToSearchScreen)

                awaitItem() shouldBe HomeScreenEvent.NavigateToSearch
            }
        }

    @Test
    fun `when going to weather details then navigation to weather details event should be emitted`() =
        runTest {
            viewModel.event.test {
                viewModel.onAction(HomeScreenAction.GoToWeatherDetails("Test", 1.0, 1.0))

                awaitItem() shouldBe HomeScreenEvent.NavigateToWeatherDetails("Test", 1.0, 1.0)
            }
        }
}