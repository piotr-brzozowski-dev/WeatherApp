package com.example.weatherapp.weatherdetails

import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class WeatherDetailsRepositoryTest {

    private val weatherDetailsApi = relaxedMock<WeatherDetailsApi>()
    private val weatherDetailsMapper = relaxedMock<WeatherDetailsMapper>()

    private val weatherDetailsRepository =
        WeatherDetailsRepository(weatherDetailsApi, weatherDetailsMapper)

    @Test
    fun `when fetching weather details then weather details should be returned`() = runTest {
        val weatherDetailsDto = relaxedMock<WeatherDetailsDto>()
        val weatherDetails = relaxedMock<WeatherDetails>()
        coEvery { weatherDetailsMapper.map("Test", weatherDetailsDto) } returns weatherDetails
        coEvery { weatherDetailsApi.getWeatherDetails(1.0, 1.0) } returns weatherDetailsDto

        val result = weatherDetailsRepository.getWeatherDetails("Test", 1.0, 1.0)

        result shouldBe Result.success(weatherDetails)
    }

    @Test
    fun `when fetching weather details with error then failure result should be returned`() =
        runTest {
            coEvery { weatherDetailsApi.getWeatherDetails(1.0, 1.0) } throws Exception("error")

            val result = weatherDetailsRepository.getWeatherDetails("Test", 1.0, 1.0)

            result.isFailure shouldBe true
        }

    @Test
    fun `when fetching weather basic info then weather details should be returned`() = runTest {
        val weatherBasicInfoDto = relaxedMock<WeatherBasicInfoDto>()
        val weatherBasicInfo = relaxedMock<WeatherBasicInfo>()
        coEvery { weatherDetailsMapper.map("Test", weatherBasicInfoDto) } returns weatherBasicInfo
        coEvery { weatherDetailsApi.getWeatherBasicInfo(1.0, 1.0) } returns weatherBasicInfoDto

        val result = weatherDetailsRepository.getWeatherBasicInfo("Test", 1.0, 1.0)

        result shouldBe Result.success(weatherBasicInfo)
    }

    @Test
    fun `when fetching weather basic info with error then failure result should be returned`() =
        runTest {
            coEvery { weatherDetailsApi.getWeatherBasicInfo(1.0, 1.0) } throws Exception("error")

            val result = weatherDetailsRepository.getWeatherBasicInfo("Test", 1.0, 1.0)

            result.isFailure shouldBe true
        }
}