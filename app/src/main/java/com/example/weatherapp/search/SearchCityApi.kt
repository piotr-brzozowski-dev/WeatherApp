package com.example.weatherapp.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchCityApi {
    @GET("v1/search?count=10&language=pl&format=json")
    suspend fun getSearchResults(@Query("name") name: String): SearchResultsDto
}