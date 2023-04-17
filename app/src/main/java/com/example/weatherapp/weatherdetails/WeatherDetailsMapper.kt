package com.example.weatherapp.weatherdetails

import android.content.Context
import com.example.weatherapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherDetailsMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun map(cityName: String, weatherDetailsDto: WeatherDetailsDto): WeatherDetails = weatherDetailsDto.let {
        WeatherDetails(
            cityName = cityName,
            latitude = it.latitude,
            longitude = it.longitude,
            weatherComponents =  map(cityName, it.currentWeather, it.dailyWeather, it.dailyUnits) + map(it.dailyWeather, it.dailyUnits)
        )
    }

    fun map(cityName: String, weatherBasicInfoDto: WeatherBasicInfoDto): WeatherBasicInfo = weatherBasicInfoDto.let {
        WeatherBasicInfo(
            cityName = cityName,
            latitude = it.latitude,
            longitude = it.longitude,
            temperature = "${it.currentWeather.temperature} ${it.dailyUnits.temperature}"
        )
    }

    private fun map(
        cityName: String,
        currentWeatherDto: CurrentWeatherDto,
        dailyWeatherDto: DailyWeatherDto,
        unitsDto: DailyUnitsDto,
    ): List<WeatherComponent> = currentWeatherDto.let {
        val currentWeatherIndex = dailyWeatherDto.time.indexOf(it.time.date)
        val (tempUnit, windSpeedUnit, rainSumUnit) = unitsDto
        listOf(
            WeatherComponent.Header(cityName),
            WeatherComponent.Label("${it.temperature} $tempUnit"),
            WeatherComponent.GridRow(
                listOf(
                    WeatherComponent.GridItem(
                        context.getString(R.string.wind_speed_label),
                        "${it.windSpeed} $windSpeedUnit"
                    ),
                    WeatherComponent.GridItem(
                        context.getString(R.string.wind_direction_label),
                        it.windDirection.toString()
                    )
                )
            ),
            WeatherComponent.GridRow(
                listOf(
                    WeatherComponent.GridItem(
                        context.getString(R.string.rain_sum_label),
                        "${dailyWeatherDto.rainSum[currentWeatherIndex]} $rainSumUnit"
                    ),
                    WeatherComponent.GridItem(
                        context.getString(R.string.uv_index_label),
                        dailyWeatherDto.uvIndex[currentWeatherIndex].toString()
                    )
                )
            ),
            WeatherComponent.GridRow(
                listOf(
                    WeatherComponent.GridItem(
                        context.getString(R.string.sunrise_label),
                        dailyWeatherDto.sunrise[currentWeatherIndex].time.toString()
                    ),
                    WeatherComponent.GridItem(
                        context.getString(R.string.sunset_label),
                        dailyWeatherDto.sunset[currentWeatherIndex].time.toString()
                    )
                )
            )
        )
    }

    private fun map(dailyWeatherDto: DailyWeatherDto, unitsDto: DailyUnitsDto): WeatherComponent {
        val boxElements = dailyWeatherDto.time.mapIndexed { index, time ->
            val (tempUnit, _, rainSumUnit) = unitsDto
            WeatherComponent.HorizontalListItem(
                listOf(
                    WeatherComponent.SmallLabel(
                        dailyWeatherDto.time[index].dayOfWeek.toString()
                    ),
                    WeatherComponent.SmallLabel(
                        context.getString(
                            R.string.forecast_min_temp_label,
                            "${dailyWeatherDto.minTemperature[index]} $tempUnit"
                        )
                    ),
                    WeatherComponent.SmallLabel(
                        context.getString(
                            R.string.forecast_max_temp_label,
                            "${dailyWeatherDto.maxTemperature[index]} $tempUnit"
                        )
                    ),
                    WeatherComponent.SmallLabel(
                        context.getString(
                            R.string.forecast_rain_sum_label,
                            "${dailyWeatherDto.rainSum[index]} $rainSumUnit"
                        )
                    ),
                )
            )
        }
        return WeatherComponent.Box(
            context.getString(R.string.ten_days_forecast_label),
            boxElements
        )
    }

}