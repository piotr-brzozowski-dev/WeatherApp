package com.example.weatherapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.locationlist.GetLocationsUseCase
import com.example.weatherapp.runSuspendCatching
import com.example.weatherapp.search.SearchCityViewEvent
import com.example.weatherapp.weatherdetails.WeatherDetailsApi
import com.example.weatherapp.weatherdetails.WeatherDetailsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val weatherDetailsApi: WeatherDetailsApi,
    private val weatherDetailsMapper: WeatherDetailsMapper
): ViewModel() {
    private val _event: Channel<HomeScreenEvent> = Channel()
    val event = _event.receiveAsFlow()

    private val _state: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Loading)
    val state = _state.asStateFlow()

    fun onAction(homeScreenAction: HomeScreenAction) {
        when (homeScreenAction) {
            is HomeScreenAction.LoadScreen -> loadData()
            HomeScreenAction.GoToSearchScreen -> navigateToSearchScreen()
            is HomeScreenAction.GoToWeatherDetails -> navigateToWeatherDetails(homeScreenAction)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = getLocationsUseCase.execute()
                .mapNotNull {location ->
                    runSuspendCatching {
                        weatherDetailsApi.getWeatherBasicInfo(
                            location.latitude,
                            location.longitude
                        )
                    }.fold({
                        weatherDetailsMapper.map(location.name, it)
                    }, { null })
                }
            _state.emit(HomeScreenState.Loaded(result))
        }
    }

    private fun navigateToWeatherDetails(action: HomeScreenAction.GoToWeatherDetails) {
        val (name, latitude, longitude) = action
        viewModelScope.launch {
            _event.send(HomeScreenEvent.NavigateToWeatherDetails(name, latitude, longitude))
        }
    }

    private fun navigateToSearchScreen() {
        viewModelScope.launch {
            _event.send(HomeScreenEvent.NavigateToSearch)
        }
    }

}