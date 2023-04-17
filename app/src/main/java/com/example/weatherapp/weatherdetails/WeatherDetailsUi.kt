package com.example.weatherapp.weatherdetails

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.ui.FontSize
import com.example.weatherapp.ui.Size

@Composable
fun WeatherDetailsUi(
    weatherDetailsViewState: State<WeatherDetailsViewState>,
    actionHandler: (WeatherDetailsViewAction) -> Unit
) {
    when (val currentState = weatherDetailsViewState.value) {
        is WeatherDetailsViewState.Loaded -> LoadedWeatherDetailsUi(
            currentState.weatherDetails,
            currentState.editMode,
            actionHandler
        )
        WeatherDetailsViewState.Loading -> LoadingWeatherDetailsUi()
    }
}

@Composable
private fun LoadedWeatherDetailsUi(
    weatherDetails: WeatherDetails,
    editMode: EditMode,
    actionHandler: (WeatherDetailsViewAction) -> Unit
) {
    with(weatherDetails) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Size.space2XS),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(Size.spaceM))
                when (editMode) {
                    EditMode.ADD -> WeatherDetailsViewAction.AddLocation(
                        cityName,
                        latitude,
                        longitude
                    )
                    EditMode.DELETE -> WeatherDetailsViewAction.DeleteLocation(
                        cityName,
                        latitude,
                        longitude
                    )
                    EditMode.READ_ONLY -> null
                }?.let {
                    Button(onClick = {
                        actionHandler(it)
                    }) {
                        Text(text = editMode.toString(), fontSize = FontSize.medium)
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Size.spaceM))
                Text(text = cityName, fontSize = FontSize.largeHeader)
                Text(
                    text = "${currentWeather.temperature} ${units.temperatureUnit}",
                    fontSize = FontSize.large
                )
                WeatherParametersRowUi {
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "Wind speed",
                        value = "${currentWeather.windSpeed} ${units.windSpeedUnit}",
                    )
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "Wind direction",
                        value = "${currentWeather.windDirection}",
                    )
                }
                WeatherParametersRowUi {
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "Rain sum",
                        value = "${currentWeather.rainSum} ${units.rainSumUnit}",
                    )
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "UV index",
                        value = currentWeather.uvIndex.toString(),
                    )
                }
                WeatherParametersRowUi {
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "Sunrise",
                        value = currentWeather.sunrise.time.toString(),
                    )
                    WeatherParameterUi(
                        modifier = Modifier.weight(1f),
                        header = "Sunset",
                        value = currentWeather.sunset.time.toString(),
                    )
                }
            }
            WeatherForecastsUi(weatherDetails.forecast)
        }
    }
}

@Composable
private fun WeatherForecastsUi(forecast: List<Forecast>) {
    Text(
        text = "10-Day forecast",
        fontSize = FontSize.medium
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(Size.space2XS, Color.LightGray, RoundedCornerShape(Size.spaceXS))
    ) {
        LazyColumn {
            items(forecast) {
                Row(
                    modifier = Modifier
                        .padding(Size.space2XS)
                ) {
                    Text(
                        text = it.time.dayOfWeek.toString(),
                        fontSize = FontSize.small,
                    )
                    Spacer(modifier = Modifier.width(Size.spaceS))
                    Text(
                        text = "max temp: ${it.minTemperature}",
                        fontSize = FontSize.small,
                    )
                    Spacer(modifier = Modifier.width(Size.spaceS))
                    Text(
                        text = "min temp: ${it.minTemperature}",
                        fontSize = FontSize.small,
                    )
                    Spacer(modifier = Modifier.width(Size.spaceS))
                    Text(
                        text = "rain sum: ${it.rainSum}",
                        fontSize = FontSize.small,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherParametersRowUi(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .height(Size.space10XL)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
private fun WeatherParameterUi(modifier: Modifier, header: String, value: String) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(Size.spaceXS)
            .border(Size.space2XS, Color.LightGray, RoundedCornerShape(Size.spaceXS)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = header, fontSize = FontSize.small)
        Text(text = value, fontSize = FontSize.medium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun LoadingWeatherDetailsUi() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}