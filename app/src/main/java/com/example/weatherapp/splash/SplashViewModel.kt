package com.example.weatherapp.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.geo.LocationPermissionResult
import com.example.weatherapp.geo.LocationPermissionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val locationPermissionService: LocationPermissionService
) :
    ViewModel() {

    private val _state: MutableStateFlow<SplashViewState> =
        MutableStateFlow(SplashViewState.Loading)
    val state: StateFlow<SplashViewState> = _state

    fun onAction(action: SplashViewAction) {
        when (action) {
            SplashViewAction.InitializeApp -> initializeApp()
        }
    }

    private fun initializeApp() {
        viewModelScope.launch {
            locationPermissionService.requestLocationPermission().collect {
                when (it) {
                    LocationPermissionResult.SUCCESS -> _state.emit(SplashViewState.Initialized(true))
                    LocationPermissionResult.FAILURE -> _state.emit(
                        SplashViewState.Initialized(
                            false
                        )
                    )
                    LocationPermissionResult.RATIONALE -> _state.emit(
                        SplashViewState.Initialized(
                            false
                        )
                    )
                }
            }
        }
    }
}