package com.example.weatherapp.search

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchCityApi {

    @GET("v1/search?count=10&language=pl&format=json")
    suspend fun getSearchResults(@Query("name") name: String): SearchResultsDto
}

//https://geocoding-api.open-meteo.com/v1/search?name=Szczecin&count=10&language=pl&format=json