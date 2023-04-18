package com.example.weatherapp.weatherdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.locationlist.AddLocationUseCase
import com.example.weatherapp.locationlist.DeleteLocationUseCase
import com.example.weatherapp.locationlist.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WeatherDetailsViewModel @Inject constructor(
    private val weatherDetailsRepository: WeatherDetailsRepository,
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
            _event.send(WeatherDetailsEvent.CloseScreen)
        }
    }

    private fun fetchWeatherDetails() {
        viewModelScope.launch {
            val weatherDetailsArgs =
                savedStateHandle.get<WeatherDetailsArgs>(WeatherDetailsActivity.ARGS)!!
            val (cityName, latitude, longitude) = weatherDetailsArgs
            weatherDetailsRepository.getWeatherDetails(cityName, latitude, longitude)
                .onSuccess {
                    _state.emit(WeatherDetailsViewState.Loaded(it, getEditModeUseCase.execute(it)))
                }
                .onFailure {
                    _event.send(WeatherDetailsEvent.FetchWeatherDetailsFailed(it.message))
                }
        }
    }
}