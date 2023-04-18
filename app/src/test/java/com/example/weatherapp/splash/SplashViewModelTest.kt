package com.example.weatherapp.splash

import app.cash.turbine.test
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.geo.LocationPermissionResult
import com.example.weatherapp.geo.LocationPermissionService
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
internal class SplashViewModelTest {

    private val locationPermissionService = relaxedMock<LocationPermissionService>()
    private val viewModel = SplashViewModel(locationPermissionService)

    @Test
    fun `when requesting location permission with success then initialized state should be emitted`() =
        runTest {
            every { locationPermissionService.requestLocationPermission() } returns flowOf(
                LocationPermissionResult.SUCCESS
            )

            viewModel.state.test {
                awaitItem() shouldBe SplashViewState.Loading
                viewModel.onAction(SplashViewAction.InitializeApp)
                awaitItem() shouldBe SplashViewState.Initialized(true)
            }
        }

    @Test
    fun `when requesting location permission with failure then initialized state should be emitted`() =
        runTest {
            every { locationPermissionService.requestLocationPermission() } returns flowOf(
                LocationPermissionResult.FAILURE
            )

            viewModel.state.test {
                awaitItem() shouldBe SplashViewState.Loading
                viewModel.onAction(SplashViewAction.InitializeApp)
                awaitItem() shouldBe SplashViewState.Initialized(false)
            }
        }

    @Test
    fun `when requesting location permission with reationale then initialized state should be emitted`() =
        runTest {
            every { locationPermissionService.requestLocationPermission() } returns flowOf(
                LocationPermissionResult.RATIONALE
            )

            viewModel.state.test {
                awaitItem() shouldBe SplashViewState.Loading
                viewModel.onAction(SplashViewAction.InitializeApp)
                awaitItem() shouldBe SplashViewState.Initialized(false)
            }
        }
}