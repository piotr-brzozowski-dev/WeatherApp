package com.example.weatherapp.weatherdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.locationlist.AddLocationUseCase
import com.example.weatherapp.locationlist.DeleteLocationUseCase
import com.example.weatherapp.locationlist.Location
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class WeatherDetailsViewModelTest {

    private val weatherDetailsApi = relaxedMock<WeatherDetailsApi>()
    private val weatherDetailsMapper = relaxedMock<WeatherDetailsMapper>()
    private val addLocationUseCase = relaxedMock<AddLocationUseCase>()
    private val deleteLocationUseCase = relaxedMock<DeleteLocationUseCase>()
    private val getEditModeUseCase = relaxedMock<GetEditModeUseCase>()
    private val savedStateHandle = relaxedMock<SavedStateHandle>()

    private val viewModel = WeatherDetailsViewModel(
        weatherDetailsApi,
        weatherDetailsMapper,
        addLocationUseCase,
        deleteLocationUseCase,
        getEditModeUseCase,
        savedStateHandle
    )

    @Test
    fun `when fetching weather details then weather details should be emitted`() = runTest {
        val weatherDetailsDto = relaxedMock<WeatherDetailsDto>()
        val weatherDetails = relaxedMock<WeatherDetails>()
        val weatherDetailsArgs = WeatherDetailsArgs("Test", 1.0, 1.0, WeatherDetailsSource.HOME)
        every { savedStateHandle.get<WeatherDetailsArgs>(WeatherDetailsActivity.ARGS) } returns weatherDetailsArgs
        coEvery { weatherDetailsMapper.map("Test", weatherDetailsDto) } returns weatherDetails
        coEvery { weatherDetailsApi.getWeatherDetails(1.0, 1.0) } returns weatherDetailsDto
        coEvery { getEditModeUseCase.execute(weatherDetails) } returns EditMode.READ_ONLY

        viewModel.state.test {
            viewModel.onAction(WeatherDetailsViewAction.LoadWeatherDetails)
            awaitItem() shouldBe WeatherDetailsViewState.Loading
            awaitItem() shouldBe WeatherDetailsViewState.Loaded(weatherDetails, EditMode.READ_ONLY)
        }
    }

    @Test
    fun `when fetching weather details with error then weather details should be emitted`() =
        runTest {
            val weatherDetailsArgs = WeatherDetailsArgs("Test", 1.0, 1.0, WeatherDetailsSource.HOME)
            every { savedStateHandle.get<WeatherDetailsArgs>(WeatherDetailsActivity.ARGS) } returns weatherDetailsArgs
            coEvery { weatherDetailsApi.getWeatherDetails(1.0, 1.0) } throws Exception("error")

            val stateTurbine = viewModel.state.testIn(this)
            val eventTurbine = viewModel.event.testIn(this)
            viewModel.onAction(WeatherDetailsViewAction.LoadWeatherDetails)
            stateTurbine.awaitItem() shouldBe WeatherDetailsViewState.Loading
            eventTurbine.awaitItem() shouldBe WeatherDetailsEvent.FetchWeatherDetailsFailed("error")
            stateTurbine.cancel()
            eventTurbine.cancel()
        }

    @Test
    fun `when add location to favourites then updated stated should be emitted`() =
        runTest {
            viewModel.onAction(WeatherDetailsViewAction.AddLocation("Test", 1.0, 1.0))

            viewModel.event.test {
                awaitItem() shouldBe WeatherDetailsEvent.CloseScreen
            }

            coVerify { addLocationUseCase.execute(Location("Test", 1.0, 1.0)) }
        }

    @Test
    fun `when delete location from favourites then close screen event should be emitted`() =
        runTest {
            viewModel.onAction(WeatherDetailsViewAction.DeleteLocation("Test", 1.0, 1.0))

            viewModel.event.test {
                awaitItem() shouldBe WeatherDetailsEvent.CloseScreen
            }

            coVerify { deleteLocationUseCase.execute(Location("Test", 1.0, 1.0)) }
        }
}