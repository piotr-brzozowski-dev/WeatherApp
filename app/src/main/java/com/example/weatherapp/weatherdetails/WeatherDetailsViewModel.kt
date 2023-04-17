package com.example.weatherapp.weatherdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.locationlist.AddLocationUseCase
import com.example.weatherapp.locationlist.DeleteLocationUseCase
import com.example.weatherapp.locationlist.Location
import com.example.weatherapp.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherDetailsViewModel @Inject constructor(
    private val weatherDetailsApi: WeatherDetailsApi,
    private val weatherDetailsMapper: WeatherDetailsMapper,
    private val addLocationUseCase: AddLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val getEditModeUseCase: GetEditModeUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _event: Channel<WeatherDetailsEvent> = Channel()
    val event = _event.receiveAsFlow()

    private val _state: MutableStateFlow<WeatherDetailsViewState> =
        MutableStateFlow(WeatherDetailsViewState.Loading)
    val state = _state.asStateFlow()

    fun onAction(weatherDetailsViewAction: WeatherDetailsViewAction) {
        when (weatherDetailsViewAction) {
            is WeatherDetailsViewAction.LoadWeatherDetails -> fetchWeatherDetails()
            is WeatherDetailsViewAction.AddLocation -> addLocationToFavourites(
                weatherDetailsViewAction
            )
            is WeatherDetailsViewAction.DeleteLocation -> removeLocationFromFavourites(
                weatherDetailsViewAction
            )
        }
    }

    private fun removeLocationFromFavourites(action: WeatherDetailsViewAction.DeleteLocation) {
        viewModelScope.launch {
            with(action) {
                val location = Location(locationName, latitude, longitude)
                deleteLocationUseCase.execute(location)
            }
            _event.send(WeatherDetailsEvent.CloseScreen)
        }
    }

    private fun addLocationToFavourites(action: WeatherDetailsViewAction.AddLocation) {
        viewModelScope.launch {
            with(action) {
                val location = Location(locationName, latitude, longitude)
                addLocationUseCase.execute(location)
            }
            val prevState = _state.value
            if (prevState is WeatherDetailsViewState.Loaded) {
                val previousResults = prevState.weatherDetails
                _state.emit(WeatherDetailsViewState.Loaded(previousResults, EditMode.READ_ONLY))
            }
        }
    }

    private fun fetchWeatherDetails() {
        viewModelScope.launch {
            val weatherDetailsArgs =
                savedStateHandle.get<WeatherDetailsArgs>(WeatherDetailsActivity.ARGS)!!
            runSuspendCatching {
                weatherDetailsApi.getWeatherDetails(
                    weatherDetailsArgs.latitude, weatherDetailsArgs.longitude
                )
            }.onSuccess {
                val result = weatherDetailsMapper.map(weatherDetailsArgs.cityName, it)
                _state.emit(
                    WeatherDetailsViewState.Loaded(
                        result,
                        getEditModeUseCase.execute(result)
                    )
                )
            }.onFailure {
                _event.send(WeatherDetailsEvent.FetchWeatherDetailsFailed(it.message))
            }
        }
    }
}