package com.example.weatherapp.search

sealed class SearchCityViewState {
    data class SearchCityResultsLoaded(val searchResults: List<SearchResult>): SearchCityViewState()
    data class SearchFailed(val message: String?): SearchCityViewState()
}