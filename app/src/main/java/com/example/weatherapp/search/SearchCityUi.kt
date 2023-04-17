package com.example.weatherapp.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.fragment.app.FragmentActivity
import com.example.weatherapp.search.SearchCityActivity.Companion.showErrorDialog
import com.example.weatherapp.ui.FontSize
import com.example.weatherapp.ui.SearchBarUi
import com.example.weatherapp.ui.Size

@Composable
fun SearchCityUi(
    searchCityViewState: State<SearchCityViewState>,
    actionHandler: (SearchCityViewAction) -> Unit
) {
    val context = LocalContext.current
    Column {
        SearchBarUi(onValueChanged = {
            actionHandler(SearchCityViewAction.SearchCityForResults(it))
        })
        when (val currentValue = searchCityViewState.value) {
            is SearchCityViewState.SearchCityResultsLoaded -> {
                SearchResultsUi(currentValue.searchResults, actionHandler)
            }
            is SearchCityViewState.SearchFailed ->
                (context as FragmentActivity).showErrorDialog(currentValue.message)
        }
    }
}

@Composable
private fun SearchResultsUi(searchResults: List<SearchResult>,
                            actionHandler: (SearchCityViewAction) -> Unit) {
    LazyColumn {
        items(searchResults) {
            Row(modifier = Modifier
                .padding(Size.space2XS)
                .clickable {
                    actionHandler(
                        SearchCityViewAction.GoToWeatherDetails(it.name, it.latitude, it.longitude)
                    )
                }
            ) {
                Text(
                    text = it.details,
                    maxLines = 1,
                    fontSize = FontSize.small,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}