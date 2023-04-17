package com.example.weatherapp.weatherdetails

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.ui.FontSize
import com.example.weatherapp.ui.Size

@Composable
fun WeatherDetailsUi(
    weatherDetailsViewState: State<WeatherDetailsViewState>,
    actionHandler: (WeatherDetailsViewAction) -> Unit
) {
    when (val currentState = weatherDetailsViewState.value) {
        is WeatherDetailsViewState.Loaded -> LoadedWeatherDetailsUi(
            currentState.weatherDetails,
            currentState.editMode,
            actionHandler
        )
        WeatherDetailsViewState.Loading -> LoadingWeatherDetailsUi()
    }
}

@Composable
private fun LoadedWeatherDetailsUi(
    weatherDetails: WeatherDetails,
    editMode: EditMode,
    actionHandler: (WeatherDetailsViewAction) -> Unit
) {
    with(weatherDetails) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Size.space2XS),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.height(Size.spaceM))
                when (editMode) {
                    EditMode.ADD -> WeatherDetailsViewAction.AddLocation(
                        cityName,
                        latitude,
                        longitude
                    )
                    EditMode.DELETE -> WeatherDetailsViewAction.DeleteLocation(
                        cityName,
                        latitude,
                        longitude
                    )
                    EditMode.READ_ONLY -> null
                }?.let {
                    Button(onClick = {
                        actionHandler(it)
                    }) {
                        Text(text = editMode.toString(), fontSize = FontSize.medium)
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Size.spaceM))
                weatherComponents.map {
                    WeatherComponent(it)
                }
            }
        }
    }
}

@Composable
private fun WeatherComponent(item: WeatherComponent) {
    item.let {
        when(it){
            is WeatherComponent.Box -> BoxUi(it.label, it.elements)
            is WeatherComponent.Label -> LabelUi(it.value)
            is WeatherComponent.GridItem -> GridItemUi(it.label, it.value)
            is WeatherComponent.HorizontalListItem -> HorizontalListItemUi(it.items)
            is WeatherComponent.GridRow -> GridRowUi(it.items)
            is WeatherComponent.Header -> HeaderUi(it.value)
            is WeatherComponent.SmallLabel -> SmallLabel(it.value)
        }
    }
}

@Composable
private fun HeaderUi(value: String) {
    Text(text = value, fontSize = FontSize.largeHeader)
}

@Composable
private fun LabelUi(value: String) {
    Text(
        text = value,
        fontSize = FontSize.large
    )
}

@Composable
private fun GridItemUi(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(Size.spaceXS)
            .border(Size.space2XS, Color.LightGray, RoundedCornerShape(Size.spaceXS)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = label, fontSize = FontSize.small)
        Text(text = value, fontSize = FontSize.medium, textAlign = TextAlign.Center)
    }
}
@Composable
private fun GridRowUi(items: List<WeatherComponent.GridItem>) {
    Row(
        modifier = Modifier
            .height(Size.space10XL)
            .fillMaxWidth()
    ) {
        items.map {
            GridItemUi(it.label, it.value, Modifier.weight(1f))
        }
    }
}

@Composable
private fun BoxUi(label: String, items: List<WeatherComponent>) {
    Text(
        text = label,
        fontSize = FontSize.medium
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(Size.space2XS, Color.LightGray, RoundedCornerShape(Size.spaceXS))
    ) {
        LazyColumn {
            items(items) {
                WeatherComponent(it)
            }
        }
    }
}

@Composable
private fun HorizontalListItemUi(items: List<WeatherComponent>) {
    Row(
        modifier = Modifier
            .padding(Size.space2XS)
    ) {
        items.map {
            WeatherComponent(it)
            Spacer(modifier = Modifier.width(Size.spaceS))
        }
    }
}

@Composable
private fun SmallLabel(value: String) {
    Text(
        text = value,
        fontSize = FontSize.extraSmall,
    )
}
@Composable
private fun LoadingWeatherDetailsUi() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}