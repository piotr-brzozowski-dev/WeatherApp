package com.example.weatherapp.search

import com.example.weatherapp.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResultRepository @Inject constructor(
    private val searchCityApi: SearchCityApi,
    private val searchResultMapper: SearchResultMapper
) {

    suspend fun getSearchResults(searchPhrase: String) = runSuspendCatching {
        val results = searchCityApi.getSearchResults(searchPhrase)
        searchResultMapper.map(results)
    }
}