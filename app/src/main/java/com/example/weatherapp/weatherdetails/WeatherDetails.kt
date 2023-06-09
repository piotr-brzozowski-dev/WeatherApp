package com.example.weatherapp.weatherdetails

data class WeatherDetails(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val weatherComponents: List<WeatherComponent>
)

data class WeatherBasicInfo(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: String
)

sealed class WeatherComponent(
    val type: WeatherComponentType
) {
    data class Box(val label: String, val elements: List<WeatherComponent>) :
        WeatherComponent(WeatherComponentType.BOX)

    data class Label(val value: String) :
        WeatherComponent(WeatherComponentType.LABEL)

    data class SmallLabel(val value: String) :
        WeatherComponent(WeatherComponentType.LABEL)

    data class Header(val value: String) :
        WeatherComponent(WeatherComponentType.HEADER)

    data class GridRow(val items: List<GridItem>) :
        WeatherComponent(WeatherComponentType.GRID_ROW)

    data class GridItem(val label: String, val value: String) :
        WeatherComponent(WeatherComponentType.GRID_ITEM)

    data class HorizontalListItem(val items: List<WeatherComponent>) :
        WeatherComponent(WeatherComponentType.HORIZONTAL_LIST_ITEM)
}

enum class WeatherComponentType {
    HEADER, BOX, LABEL, GRID_ROW, GRID_ITEM, HORIZONTAL_LIST_ITEM
}