package com.example.weatherapp.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R
import com.example.weatherapp.ui.FontSize
import com.example.weatherapp.ui.SearchBarUi
import com.example.weatherapp.ui.Size
import com.example.weatherapp.weatherdetails.WeatherBasicInfo

@Composable
fun HomeUi(
    homeScreenState: State<HomeScreenState>,
    actionHandler: (HomeScreenAction) -> Unit
) {
    when (val currentState = homeScreenState.value) {
        is HomeScreenState.Loaded -> LoadedHomeUi(currentState.savedLocation, actionHandler)
        HomeScreenState.Loading -> LoadingHomeUi()
    }
}

@Composable
fun LoadedHomeUi(
    savedLocations: List<WeatherBasicInfo>,
    actionHandler: (HomeScreenAction) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.weather_title),
            fontSize = FontSize.large
        )
        Spacer(modifier = Modifier.height(Size.spaceXS))
        SearchBarUi(enabled = false, onClick = {
            actionHandler(HomeScreenAction.GoToSearchScreen)
        })
        LoadedChannelSelector(savedLocations, actionHandler)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadedChannelSelector(
    savedLocations: List<WeatherBasicInfo>,
    actionHandler: (action: HomeScreenAction) -> Unit
) {
    LazyColumn {
        items(savedLocations) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(Size.spaceS),
                shape = RoundedCornerShape(Size.spaceXS),
                onClick = {
                    actionHandler(
                        HomeScreenAction.GoToWeatherDetails(
                            it.cityName,
                            it.latitude,
                            it.longitude
                        )
                    )
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        text = it.cityName,
                        fontSize = FontSize.medium
                    )
                    Text(
                        modifier = Modifier,
                        text = it.temperature,
                        fontSize = FontSize.large
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingHomeUi() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}