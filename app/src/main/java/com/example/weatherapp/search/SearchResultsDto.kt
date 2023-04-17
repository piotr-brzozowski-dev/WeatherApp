package com.example.weatherapp.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultsDto(
    val results: List<SearchResultItemDto>? = null
)

@Serializable
data class SearchResultItemDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("country_code") val countryCode: String?,
    val country: String?,
    val admin1: String?,
    val admin2: String?,
    val admin3: String?
)
//{
//    "id": 3083830,
//    "name": "Szczecin",
//    "latitude": 51.91848,
//    "longitude": 19.71149,
//    "elevation": 147.0,
//    "feature_code": "PPL",
//    "country_code": "PL",
//    "admin1_id": 3337493,
//    "admin2_id": 7531018,
//    "admin3_id": 7531130,
//    "timezone": "Europe/Warsaw",
//    "country_id": 798544,
//    "country": "Polska",
//    "admin1": "Województwo łódzkie",
//    "admin2": "Powiat brzeziński",
//    "admin3": "Dmosin"
//}
