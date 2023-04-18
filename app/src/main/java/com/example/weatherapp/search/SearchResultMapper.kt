package com.example.weatherapp.search

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResultMapper @Inject constructor() {

    fun map(searchResults: SearchResultsDto): List<SearchResult> = searchResults
        .results?.map {
            val details =
                listOfNotNull(
                    it.name,
                    it.countryCode,
                    it.country,
                    it.admin1,
                    it.admin2,
                    it.admin3
                ).joinToString()
            SearchResult(
                name = it.name,
                details = details,
                latitude = it.latitude,
                longitude = it.longitude,
            )
        } ?: emptyList()
}