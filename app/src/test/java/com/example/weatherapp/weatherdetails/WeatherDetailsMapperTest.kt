package com.example.weatherapp.weatherdetails

import android.content.Context
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class WeatherDetailsMapperTest {

    private val context = relaxedMock<Context>()
    private val mapper = WeatherDetailsMapper(context)

    @Test
    fun `when mapping weather basic info dto then mapper should return weather basic info`() {
        val weatherBasicInfoDto = WeatherBasicInfoDto(
            latitude = 1.0,
            longitude = 1.0,
            currentWeather = CurrentWeatherDto(
                temperature = 10.0f,
                windSpeed = 20.0f,
                windDirection = 120.0f,
                time = LocalDateTime.parse("2023-04-17T01:00"),
                isDay = 1
            ),
            dailyUnits = DailyUnitsDto("C", "mm", "km/h")
        )

        val result = mapper.map("Test", weatherBasicInfoDto)

        result shouldBe WeatherBasicInfo("Test", 1.0, 1.0, "10.0 C")
    }

    @Test
    fun `when mapping weather info dto then mapper should return weather info`() {
        val labelRes = "label"
        every { context.getString(any()) } returns labelRes
        every { context.getString(any(), any()) } returns labelRes
        val weatherDetailsDto = getWeatherDetailsDto()

        val result = mapper.map("test", weatherDetailsDto)

        result shouldBe getExpectedWeatherDetails(labelRes)
    }

    companion object {
        fun getWeatherDetailsDto(): WeatherDetailsDto = WeatherDetailsDto(
            latitude = 1.0,
            longitude = 1.0,
            currentWeather = CurrentWeatherDto(
                temperature = 10.0f,
                windSpeed = 20.0f,
                windDirection = 120.0f,
                time = LocalDateTime.parse("2023-04-17T01:00"),
                isDay = 1
            ),
            dailyUnits = DailyUnitsDto("C", "mm", "km/h"),
            dailyWeather = DailyWeatherDto(
                time = listOf(
                    LocalDate.parse("2023-04-17"),
                    LocalDate.parse("2023-04-18")
                ),
                maxTemperature = listOf(10.0f, 20.0f),
                minTemperature = listOf(1.0f, 2.0f),
                sunrise = listOf(
                    LocalDateTime.parse("2023-04-17T01:00"),
                    LocalDateTime.parse("2023-04-18T01:00")
                ),
                sunset = listOf(
                    LocalDateTime.parse("2023-04-17T01:00"),
                    LocalDateTime.parse("2023-04-18T01:00")
                ),
                uvIndex = listOf(9.0f, 10.0f),
                rainSum = listOf(100.0f, 123.0f),
                windSpeed = listOf(23.0f, 26.0f)
            )
        )

        fun getExpectedWeatherDetails(label: String): WeatherDetails = WeatherDetails(
            cityName = "test",
            latitude = 1.0,
            longitude = 1.0,
            weatherComponents = getExpectedWeatherComponent(label)
        )

        private fun getExpectedWeatherComponent(label: String): List<WeatherComponent> = listOf(
            WeatherComponent.Header("test"),
            WeatherComponent.Label("10.0 C"),
            WeatherComponent.GridRow(
                items = listOf(
                    WeatherComponent.GridItem(
                        label = label,
                        value = "20.0 mm"
                    ),
                    WeatherComponent.GridItem(
                        label = label,
                        value = "120.0"
                    )
                )
            ),
            WeatherComponent.GridRow(
                items = listOf(
                    WeatherComponent.GridItem(
                        label = label,
                        value = "100.0 km/h"
                    ),
                    WeatherComponent.GridItem(
                        label = label,
                        value = "9.0"
                    )
                )
            ),
            WeatherComponent.GridRow(
                items = listOf(
                    WeatherComponent.GridItem(
                        label = label,
                        value = "01:00"
                    ),
                    WeatherComponent.GridItem(
                        label = label,
                        value = "01:00"
                    )
                )
            ),
            WeatherComponent.Box(
                label = label,
                elements = listOf(
                    WeatherComponent.HorizontalListItem(
                        items = listOf(
                            WeatherComponent.SmallLabel("MONDAY"),
                            WeatherComponent.SmallLabel(label),
                            WeatherComponent.SmallLabel(label),
                            WeatherComponent.SmallLabel(label)
                        )
                    ),
                    WeatherComponent.HorizontalListItem(
                        items = listOf(
                            WeatherComponent.SmallLabel("TUESDAY"),
                            WeatherComponent.SmallLabel(label),
                            WeatherComponent.SmallLabel(label),
                            WeatherComponent.SmallLabel(label)
                        )
                    )
                )
            )
        )
    }
}