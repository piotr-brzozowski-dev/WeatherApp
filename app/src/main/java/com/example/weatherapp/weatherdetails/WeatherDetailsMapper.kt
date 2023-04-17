package com.example.weatherapp.weatherdetails

import javax.inject.Inject

class WeatherDetailsMapper @Inject constructor() {
    fun map(cityName: String, weatherDetailsDto: WeatherDetailsDto): WeatherDetails = weatherDetailsDto.let {
        WeatherDetails(
            cityName = cityName,
            latitude = it.latitude,
            longitude = it.longitude,
            currentWeather = map(it.currentWeather, it.dailyWeather),
            forecast = map(it.dailyWeather),
            units = map(it.dailyUnits)
        )
    }

    fun map(cityName: String, weatherBasicInfoDto: WeatherBasicInfoDto): WeatherBasicInfo = weatherBasicInfoDto.let {
        WeatherBasicInfo(
            cityName = cityName,
            latitude = it.latitude,
            longitude = it.longitude,
            temperature = it.currentWeather.temperature,
            temperatureUnit = it.dailyUnits.temperature
        )
    }

    private fun map(
        currentWeatherDto: CurrentWeatherDto,
        dailyWeatherDto: DailyWeatherDto
    ): CurrentWeather = currentWeatherDto.let {
        val currentWeatherIndex = dailyWeatherDto.time.indexOf(it.time.date)
        CurrentWeather(
            time = it.time,
            temperature = it.temperature,
            windSpeed = it.windSpeed,
            windDirection = it.windDirection,
            isDay = it.isDay == 1,
            sunrise = dailyWeatherDto.sunrise[currentWeatherIndex],
            sunset = dailyWeatherDto.sunset[currentWeatherIndex],
            rainSum = dailyWeatherDto.rainSum[currentWeatherIndex],
            maxTemperature = dailyWeatherDto.maxTemperature[currentWeatherIndex],
            minTemperature = dailyWeatherDto.minTemperature[currentWeatherIndex],
            uvIndex = dailyWeatherDto.uvIndex[currentWeatherIndex]
        )
    }

    private fun map(dailyWeatherDto: DailyWeatherDto): List<Forecast> =
        dailyWeatherDto.time.mapIndexed { index, time ->
            Forecast(
                time = time,
                minTemperature = dailyWeatherDto.minTemperature[index],
                maxTemperature = dailyWeatherDto.maxTemperature[index],
                rainSum = dailyWeatherDto.rainSum[index]
            )
        }

    private fun map(dailyUnitsDto: DailyUnitsDto): Units = dailyUnitsDto.let {
        Units(
            temperatureUnit = it.temperature,
            rainSumUnit = it.rainSum,
            windSpeedUnit = it.windSpeed
        )
    }
}