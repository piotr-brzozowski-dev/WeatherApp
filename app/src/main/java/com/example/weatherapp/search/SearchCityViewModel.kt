package com.example.weatherapp.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val searchCityApi: SearchCityApi,
    private val searchResultMapper: SearchResultMapper
): ViewModel() {

    private val _event: Channel<SearchCityViewEvent> = Channel()
    val event = _event.receiveAsFlow()

    private val _state: MutableStateFlow<SearchCityViewState> =
        MutableStateFlow(SearchCityViewState.SearchCityResultsLoaded(emptyList()))
    val state = _state.asStateFlow()

    private val _searchState: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchState.mapLatest {
                searchForCities(it)
            }.collect {
                _state.emit(it)
            }
        }
    }

    fun onAction(searchCityViewAction: SearchCityViewAction) {
        when(searchCityViewAction) {
            is SearchCityViewAction.SearchCityForResults ->
                searchForResults(searchCityViewAction.searchPhrase)
            is SearchCityViewAction.GoToWeatherDetails ->
                navigateToWeatherDetails(searchCityViewAction)
        }
    }

    private fun navigateToWeatherDetails(action: SearchCityViewAction.GoToWeatherDetails) {
        val (name, latitude, longitude) = action
        viewModelScope.launch {
            _event.send(SearchCityViewEvent.NavigateToWeatherDetails(name, latitude, longitude))
        }
    }

    private fun searchForResults(searchPhrase: String) {
        viewModelScope.launch {
            _searchState.emit(searchPhrase)
        }
    }

    private suspend fun searchForCities(searchPhrase: String): SearchCityViewState {
        return runSuspendCatching {
            searchCityApi.getSearchResults(searchPhrase)
        }.fold({
            val results = searchResultMapper.map(it)
            SearchCityViewState.SearchCityResultsLoaded(results)
        },{
            SearchCityViewState.SearchFailed(it.message)
        })
    }

}